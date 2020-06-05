package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.World;

public class EntityChipsqueek extends EntityChargingMobBase {

    public EntityAIChargeAttackBase<EntityChipsqueek> attack = new EntityAIChargeAttackBase<EntityChipsqueek>(this);
    private static final AnimatedAction melee = new AnimatedAction(11,6, "tail_slap");
    private static final AnimatedAction roll = new AnimatedAction(12,2, "roll");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee, roll};

    public EntityChipsqueek(World world) {
        super(world);
        this.tasks.addTask(2, this.attack);
        this.setSize(0.65f, 1.15f);
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim){
        return 0.75;
    }

    @Override
    public float chargingLength(){
        return 5;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        switch(type){
            case MELEE: return anim.getID().equals(melee.getID());
            case CHARGE: return anim.getID().equals(roll.getID());
        }
        return false;
    }
}
