package com.flemmli97.runecraftory.common.entity.monster.boss;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAmbrosia extends EntityBossBase{

	private Map<ItemStack, Float>drops = new HashMap<ItemStack, Float>();
	private AttackAI status = AttackAI.IDDLE;
	public EntityAmbrosia(World world) {
		super(world, true, 10, 12, true);
		this.tasks.addTask(1, new EntityAIAmbrosia(this));
		this.drops.put(new ItemStack(ModItems.strings, 1,2),0.5F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.initiateBaseAttributes(SharedMonsterAttributes.MAX_HEALTH,186);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34);;
		this.initiateBaseAttributes(ItemStats.RFATTACK,21.5);
		this.initiateBaseAttributes(ItemStats.RFDEFENCE,13.5);
		this.initiateBaseAttributes(ItemStats.RFMAGICATT,18.3);
		this.initiateBaseAttributes(ItemStats.RFMAGICDEF,14.2);
	}
	
	@Override
	public ItemStack[] tamingItem() {
		// big toyherb
		return null;
	}

	@Override
	public float tamingChance() {
		return 0.35F;
	}

	@Override
	public float attackChance() {
		return 100;
	}

	@Override
	public Map<ItemStack, Float> getDrops() {
		return drops;
	}

	/*@Override
	public EnumElement entityElement() {
		return EnumElement.EARTH;
	}*/
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(this.status==AttackAI.WAVE)
			return false;
		return super.attackEntityFrom(source, amount);
	}
	
	public void summonButterfly() {
		for(int i = 0; i < 2; i++)
		{
			if(!this.world.isRemote)
			{
				EntityButterfly fly = new EntityButterfly(this.world, this);
				fly.shoot(this, this.rotationPitch, this.rotationYawHead, 0, 0.3F, 15);
				this.world.spawnEntity(fly);
			}
		}
	}
	
	public void summonWave(int duration)
	{
		if(!this.world.isRemote)
		{
			EntityAmbrosiaWave wave = new EntityAmbrosiaWave(this.world, this, duration);
			this.world.spawnEntity(wave);
		}
	}
	
	public void summonSleepBalls()
	{
		if(!this.world.isRemote)
		{
			for(int i = 0; i < 4; i++)
			{
				double angle = i/4.0*Math.PI*2 + Math.toRadians(this.rotationYaw);
				double x = Math.cos(angle)*1.3;
				double z = Math.sin(angle)*1.3;
				EntityAmbrosiaSleep wave = new EntityAmbrosiaSleep(this.world, this);
				wave.setPosition(this.posX + x, this.posY+0.4, this.posZ + z);
				this.world.spawnEntity(wave);
			}
		}
	}
	
	public AttackAI getStatus() {
		return this.status;
	}

	public void setStatus(AttackAI status) {
		this.status = status;
	}

	public enum AttackAI
	{
		IDDLE(0,0),
		//move to target, then kick
		KICK1(36, 12),
		//summon hp draining butterflies
		BUTTERFLY(85,80),
		//use a sound wave damaging nearby entities
		WAVE(125,120),
		//summon 4 sleep inducing lights
		SLEEP(12,10),
		//move to target, then spin kick 2 times while scattering clouds of earth magic
		KICK2(36,12);
		
		private int duration, time;
		AttackAI(int attackDuration, int attackTime)
		{
			this.duration=attackDuration;
			this.time=attackTime;
		}
		
		public int getDuration()
		{
			return this.duration;
		}
		
		public int getTime()
		{
			return this.time;
		}
	}
}
