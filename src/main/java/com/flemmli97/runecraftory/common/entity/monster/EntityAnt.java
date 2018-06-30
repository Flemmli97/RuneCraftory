package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;

import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase
{
    public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1.0, true, 1.0f);
    
    public EntityAnt(World world) {
        super(world);
        this.setSize(0.6f, 0.45f);
        this.tasks.addTask(2, this.attack);
    }
    
    public float attackChance() {
        return 0.6f;
    }
    
    @Override
    public int getAttackTimeFromPattern(byte pattern) {
        return 10;
    }
    
    @Override
    public int attackFromPattern() {
        return 7;
    }
    
    @Override
    public int maxAttackPatterns() {
        return 1;
    }
}
