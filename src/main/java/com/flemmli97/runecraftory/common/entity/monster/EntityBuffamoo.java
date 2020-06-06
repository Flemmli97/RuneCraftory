package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityBuffamoo extends EntityChargingMobBase {

    private EntityAIChargeAttackBase<EntityBuffamoo> ai = new EntityAIChargeAttackBase<EntityBuffamoo>(this);
    private static final AnimatedAction chargeAttack = new AnimatedAction(61,16, "charge");
    private static final AnimatedAction stamp = new AnimatedAction(8,4, "stamp");

    private static final AnimatedAction[] anims = new AnimatedAction[] {stamp, chargeAttack};

    public EntityBuffamoo(World world) {
        super(world);
        this.tasks.addTask(2, this.ai);
        this.setSize(1.1F, 1.45F);
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
    public double maxAttackRange(AnimatedAction anim){
        return 1.2;
    }

    @Override
    public float chargingLength(){
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.CHARGE) {
            return anim.getID().equals(chargeAttack.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(stamp.getID());
    }
}
