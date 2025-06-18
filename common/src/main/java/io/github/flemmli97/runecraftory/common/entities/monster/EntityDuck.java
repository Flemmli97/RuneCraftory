package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class EntityDuck extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("slap", AnimationsBuilder.definition(0.72).marker("attack", 0.4));
    public static final String DIVE = BUILDER.add("dive", AnimationsBuilder.definition(1.84).marker("dive", 1.08).infinite());
    public static final String LAND = BUILDER.add("land", AnimationsBuilder.definition(0.48));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityDuck>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityDuck>(DIVE)
//                    .cooldown(e -> e.animationCooldown(DIVE))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 6))), 1)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityDuck>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 3)
//    );
//
//    public final AnimatedAttackGoal<EntityDuck> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityDuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityDuck(EntityType<? extends EntityDuck> type, Level world) {
        super(type, world);
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
    public void handleAttack(AnimationState anim) {
        if (anim.is(DIVE)) {
            if (anim.isPast("dive")) {
                if (this.getChargeMotion() == null) {
                    this.setChargeMotion(this.getChargeTo(anim));
                }
                this.setDeltaMovement(this.getChargeMotion().x, -0.25f, this.getChargeMotion().z);
                if (!this.onGround()) {
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
                    this.setChargeMotion(null);
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
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && (anim.is(DIVE) ? anim.isPast("dive") : anim.is(LAND));
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(DIVE) || anim.equals(LAND);
    }

    @Override
    public Vec3 getChargeTo(AnimationState anim) {
        return EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true).scale(0.7);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.8;
        double length = this.getBbWidth() * 2.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    public String getSleepAnimation() {
        return STILL;
    }

//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 24.6 / 16d, -6 / 16d);
//    }
}
