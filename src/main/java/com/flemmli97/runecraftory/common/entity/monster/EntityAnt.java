package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase
{
    public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1.0, true, 1.0f);
    
    public EntityAnt(World world) {
        super(world);
        this.setSize(0.6f, 0.45f);
        this.tasks.addTask(2, this.attack);
    }
    
    @Override
	public float attackChance() {
        return 0.6f;
    }
    
	@Override
	public AnimatedAction[] getAnimations() {
		return AnimatedAction.vanillaAttackOnly;
	}

	@Override
	public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
		return true;
	}
}
