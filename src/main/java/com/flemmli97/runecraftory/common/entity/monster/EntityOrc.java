package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;

import net.minecraft.world.World;

public class EntityOrc extends EntityMobBase
{
    public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1.0, true, 1.0f);
    
    public EntityOrc(World world) {
        super(world);
        this.tasks.addTask(2, this.attack);
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }
    
    @Override
    public int getAttackTimeFromPattern(byte pattern) {
        return 20;
    }
    
    @Override
    public int attackFromPattern() {
        return 20;
    }
    
    @Override
    public int maxAttackPatterns() {
        return 1;
    }
}
