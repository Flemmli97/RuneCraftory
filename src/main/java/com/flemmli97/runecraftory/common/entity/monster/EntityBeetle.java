package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.World;

public class EntityBeetle extends EntityChargingMobBase {

    private EntityAIChargeAttackBase<EntityBeetle> ai = new EntityAIChargeAttackBase<EntityBeetle>(this);
    private static final AnimatedAction chargeAttack = new AnimatedAction(30,2, "ramm");
    private static final AnimatedAction melee = new AnimatedAction(15,8, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee, chargeAttack};

    public EntityBeetle(World world) {
        super(world);
        this.tasks.addTask(2, this.ai);
    }

    @Override
    public float attackChance() {
        return 1.1f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public float chargingLength(){
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        switch(type){
            case CHARGE: return anim.getID().equals(chargeAttack.getID()) && this.getRNG().nextFloat() < 0.8;
            case MELEE: return anim.getID().equals(melee.getID());
        }
        return false;
    }
}
