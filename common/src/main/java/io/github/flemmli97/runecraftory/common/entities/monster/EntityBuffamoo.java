package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBuffamoo extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(44, 16, "charge");
    public static final AnimatedAction STAMP = new AnimatedAction(8, 4, "stamp");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(STAMP, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{STAMP, CHARGE_ATTACK, INTERACT, SLEEP};

    public final ChargeAttackGoal<EntityBuffamoo> ai = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityBuffamoo> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.is(CHARGE_ATTACK);
        }
        return type == AnimationType.MELEE && anim.is(STAMP);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(STAMP);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.7f;
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.7f;
    }

    @Override
    public AnimationHandler<EntityBuffamoo> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void doWhileCharge() {
        if (this.tickCount % 3 == 0)
            this.level.playSound(null, this.blockPosition(), SoundEvents.COW_STEP, this.getSoundSource(), 1, this.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public float chargingLength() {
        return 7;
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
        return new Vec3(0, 23 / 16d, -2 / 16d);
    }
}
