package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityBuffamoo extends ChargingMonster {

    public final ChargeAttackGoal<EntityBuffamoo> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(61, 16, "charge");
    public static final AnimatedAction stamp = new AnimatedAction(8, 4, "stamp");

    private static final AnimatedAction[] anims = new AnimatedAction[]{stamp, chargeAttack};

    private final AnimationHandler<EntityBuffamoo> animationHandler = new AnimationHandler<>(this, anims);

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18);
        super.applyAttributes();
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
    protected float getSoundVolume() {
        return 1.1f;
    }

    @Override
    public float getVoicePitch() {
        return 0.7f;
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
            this.level.playSound(null, this.blockPosition(), SoundEvents.COW_STEP, SoundSource.HOSTILE, 1, this.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.2;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(chargeAttack.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(stamp.getID());
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(chargeAttack);
            else
                this.getAnimationHandler().setAnimation(stamp);
        }
    }
}
