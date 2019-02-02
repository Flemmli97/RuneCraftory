package com.flemmli97.runecraftory.common.entity.monster.boss;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIThunderbolt;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityThunderbolt extends EntityBossBase{

	public EntityThunderbolt(World world) {
		super(world);
        this.tasks.addTask(1, new EntityAIThunderbolt(this));
	}
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.37);
    }

	@Override
	public float attackChance() {
		return 100;
	}
	
	public void lazerBeam()
	{
		
	}
	
	public void lazerKick()
	{
		
	}
	
	public void windBlade()
	{
		
	}

	public enum AttackAI
    {
        IDDLE(0, 0), 
        //Simple kick
        FRONTKICK(36, 12), 
        //Turns and sweeps with backlegs.
        BACKKICK(85, 80), 
        //Shoots out 5 wind lazer beams.
        LAZER5X(125, 120), 
        //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
        STOMP(12, 10), 
        //horn attack: throws player into air and then kicks him away
        HORNATTACK(0,0),
        //Charges 2 times at the player. 2-3 when enraged
        CHARGE(36, 12),
        //Shoots lazers in all directions. only enraged
        LAZERAOE(0,0),
        //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
        LAZERKICK(0,0),
        //Shoots 2 slight homing wind blades
        WINDBLADE(0,0),
        //after feinting death
        LAZERKICK3X(0,0);
        
        private int duration;
        private int time;
        
        private AttackAI(int attackDuration, int attackTime) {
            this.duration = attackDuration;
            this.time = attackTime;
        }
        
        public int getDuration() {
            return this.duration;
        }
        
        public int getTime() {
            return this.time;
        }
    }
}
