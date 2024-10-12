package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityPanther extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(16, 9, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(23, 6, "leap");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPanther>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.8f), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityPanther>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .withCondition(ActionUtils.chanced(e -> 1))
                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(1.5, 1, 4))), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityPanther>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .withCondition(ActionUtils.chanced(e -> 1))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 4))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPanther>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
    );

    public final AnimatedAttackGoal<EntityPanther> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityPanther> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPanther(EntityType<? extends EntityPanther> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ENTITY_PANTHER_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_PANTHER_AMBIENT.get();
    }

    @Override
    public AnimationHandler<? extends EntityPanther> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LEAP)) {
            this.lookAt(EntityAnchorArgument.Anchor.FEET, this.position().add(this.getDeltaMovement().x, 0, this.getDeltaMovement().z));
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.targetPosition = this.getTarget().position();
            }
            if (anim.canAttack()) {
                Vec3 vec32 = this.getLeapVec(this.getTarget() == null ? this.targetPosition : this.getTarget().position());
                this.setDeltaMovement(vec32.x, 0.25f, vec32.z);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    protected boolean isLeapingAnim(AnimatedAction anim) {
        return anim.is(LEAP);
    }

    @Override
    public Vec3 getLeapVec(@Nullable Vec3 target) {
        return super.getLeapVec(target).scale(1.15);
    }

    @Override
    public double leapHeightMotion() {
        return 0.25f;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.is(anim))
            return 2;
        return 1.5;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
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
        return SLEEP;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 21.5 / 16d, -13 / 16d);
    }
}
