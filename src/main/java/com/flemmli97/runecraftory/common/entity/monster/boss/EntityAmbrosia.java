package com.flemmli97.runecraftory.common.entity.monster.boss;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAmbrosia extends EntityBossBase
{
    private AttackAI status = AttackAI.IDDLE;
    
    public EntityAmbrosia(World world) {
        super(world);
        this.tasks.addTask(1, new EntityAIAmbrosia(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34);
    }
    
    public float attackChance() {
        return 100.0f;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.status != AttackAI.WAVE && super.attackEntityFrom(source, amount);
    }
    
    public void summonButterfly() {
        for (int i = 0; i < 2; ++i) {
            if (!this.world.isRemote) {
                EntityButterfly fly = new EntityButterfly(this.world, (EntityLivingBase)this);
                fly.shoot((Entity)this, this.rotationPitch, this.rotationYawHead, 0.0f, 0.3f, 15.0f);
                this.world.spawnEntity((Entity)fly);
            }
        }
    }
    
    public void summonWave(int duration) {
        if (!this.world.isRemote) {
            EntityAmbrosiaWave wave = new EntityAmbrosiaWave(this.world, this, duration);
            this.world.spawnEntity((Entity)wave);
        }
    }
    
    public void summonSleepBalls() {
        if (!this.world.isRemote) {
            for (int i = 0; i < 4; ++i) {
                double angle = i / 4.0 * 3.141592653589793 * 2.0 + Math.toRadians(this.rotationYaw);
                double x = Math.cos(angle) * 1.3;
                double z = Math.sin(angle) * 1.3;
                EntityAmbrosiaSleep wave = new EntityAmbrosiaSleep(this.world, this);
                wave.setPosition(this.posX + x, this.posY + 0.4, this.posZ + z);
                this.world.spawnEntity((Entity)wave);
            }
        }
    }
    
    public AttackAI getStatus() {
        return this.status;
    }
    
    public void setStatus(AttackAI status) {
        this.status = status;
    }
    
    protected void playStepSound(BlockPos pos, Block blockIn) {
    }
    
    public enum AttackAI
    {
        IDDLE(0, 0), 
        KICK1(36, 12), 
        BUTTERFLY(85, 80), 
        WAVE(125, 120), 
        SLEEP(12, 10), 
        KICK2(36, 12);
        
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
