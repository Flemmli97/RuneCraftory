package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityBeetle extends EntityMobBase {

    private EntityAIChargeAttackBase<EntityBeetle> ai = new EntityAIChargeAttackBase<EntityBeetle>(this, 1.0f);
    private static final AnimatedAction chargeAttack = new AnimatedAction(30,0, "charge");
    private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, chargeAttack};

    public EntityBeetle(World world) {
        super(world);
        this.tasks.addTask(2, this.ai);
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
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.CHARGE) {
            return anim.getID().equals("charge");
        }
        return type != AnimationType.MELEE || anim.getID().equals("vanilla");
    }
}
