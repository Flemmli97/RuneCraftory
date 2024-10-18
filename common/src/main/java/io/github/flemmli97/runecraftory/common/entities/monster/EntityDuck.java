package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityDuck extends ChargingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(15, 8, "slap");
    public static final AnimatedAction DIVE = AnimatedAction.builder((int) Math.ceil(1.84 * 20), "dive").marker((int) Math.ceil(1.08 * 20)).infinite().build();
    private static final AnimatedAction LAND = new AnimatedAction(0.48, 0, "land");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, DIVE, LAND, INTERACT, STILL};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityDuck>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityDuck>(DIVE)
                    .cooldown(e -> e.animationCooldown(DIVE))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 6))), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityDuck>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 3)
    );

    public final AnimatedAttackGoal<EntityDuck> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityDuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityDuck(EntityType<? extends EntityDuck> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public AnimationHandler<? extends EntityDuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DIVE)) {
            if (anim.isPastTick(anim.getAttackTime())) {
                if (this.chargeMotion == null) {
                    Vec3 dir = this.getTarget() != null ? this.getTarget().position().subtract(this.position()) : this.getLookAngle();
                    this.lookAt(EntityAnchorArgument.Anchor.EYES, this.position().add(dir));
                    dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.7);
                    this.chargeMotion = dir;
                    this.lockYaw(this.getYRot());
                }
                this.setDeltaMovement(this.chargeMotion.x, -0.25f, this.chargeMotion.z);
                if (!this.isOnGround()) {
                    if (this.hitEntity == null)
                        this.hitEntity = new ArrayList<>();
                    this.mobAttack(anim, null, e -> {
                        if (!this.hitEntity.contains(e)) {
                            this.hitEntity.add(e);
                            this.doHurtTarget(e);
                        }
                    });
                } else {
                    this.getAnimationHandler().setAnimation(LAND);
                    this.chargeMotion = null;
                }
            } else {
                Vec3 delta = this.getDeltaMovement();
                this.setDeltaMovement(delta.x, 0.1f, delta.z);
                if (this.getTarget() != null) {
                    this.lookAt(this.getTarget(), 20, 30);
                }
            }
        } else if (!anim.is(LAND))
            super.handleAttack(anim);
    }

    @Override
    protected boolean fixedYaw() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && (anim.is(DIVE) ? anim.isPastTick(anim.getAttackTime() + 1) : anim.is(LAND));
    }

    @Override
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(DIVE, LAND);
    }

    @Override
    public Vec3 getChargeTo(AnimatedAction anim, Vec3 pos) {
        Vec3 vec = pos.subtract(this.position());
        return new Vec3(vec.x, vec.y, vec.z);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.25;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(DIVE);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return STILL;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 24.6 / 16d, -6 / 16d);
    }
}
