package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityWolf extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(36, 10, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(23, 7, "leap");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityWolf>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.8f), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityWolf>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(1.5, 1, 4))), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityWolf>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 4))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityWolf>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
    );

    public final AnimatedAttackGoal<EntityWolf> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityWolf> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityWolf(EntityType<? extends EntityWolf> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.goalSelector.removeGoal(this.wander);
        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
        this.goalSelector.addGoal(6, this.wander);
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.6f;
    }

    @Override
    public AnimationHandler<? extends EntityWolf> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(MELEE)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.lookAtNow(this.getTarget(), 360, 90);
                this.targetPosition = this.getTarget().position();
            }
            if (anim.getTick() == 10 || anim.getTick() == 17 || anim.getTick() == 23 || anim.getTick() == 30) {
                this.mobAttack(anim, this.getTarget(), target -> wolfAttack(this, target));
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
        return super.getLeapVec(target).scale(1.1);
    }

    @Override
    public double leapHeightMotion() {
        return 0.2;
    }

    public static boolean wolfAttack(LivingEntity attacker, Entity target) {
        CustomDamage.Builder source = new CustomDamage.Builder(attacker).noKnockback().hurtResistant(1);
        return CombatUtils.mobAttack(attacker, target, source);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.is(anim))
            return 1.15;
        return 1.25;
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
        return new Vec3(0, 17 / 16d, -6 / 16d);
    }
}
