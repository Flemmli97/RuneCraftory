package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.LeapingAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityWolf extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(36, 10, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(23, 7, "leap");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    public LeapingAttackGoal<EntityWolf> attack = new LeapingAttackGoal<>(this);
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
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.7f;
        return 1;
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
    public Vec3 getLeapVec(@Nullable Vec3 target) {
        return super.getLeapVec(target).scale(1.1);
    }

    @Override
    public float maxLeapDistance() {
        return 3.5f;
    }

    @Override
    public double leapHeightMotion() {
        return 0.2;
    }

    public static boolean wolfAttack(LivingEntity attacker, Entity target) {
        CustomDamage.Builder source = new CustomDamage.Builder(attacker).noKnockback().hurtResistant(1);
        double damagePhys = CombatUtils.getAttributeValue(attacker, Attributes.ATTACK_DAMAGE);
        return CombatUtils.mobAttack(attacker, target, source, damagePhys);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.is(MELEE);
        if (type == AnimationType.LEAP)
            return anim.is(LEAP);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.is(anim))
            return 1.15;
        return 1.2;
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
}
