package com.flemmli97.runecraftory.mobs.entity.monster;

import com.flemmli97.runecraftory.mobs.entity.ChargingMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EntityBuffamoo extends ChargingMonster {

    public final ChargeAttackGoal<EntityBuffamoo> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(61, 16, "charge");
    public static final AnimatedAction stamp = new AnimatedAction(8, 4, "stamp");

    private static final AnimatedAction[] anims = new AnimatedAction[]{stamp, chargeAttack};

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.1f;
    }

    @Override
    protected float getSoundPitch() {
        return 0.7f;
    }

    @Override
    public float attackChance() {
        return 0.9f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
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
}
