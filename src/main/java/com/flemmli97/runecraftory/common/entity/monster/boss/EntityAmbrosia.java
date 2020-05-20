package com.flemmli97.runecraftory.common.entity.monster.boss;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAmbrosia extends EntityBossBase
{    
    //Tries kicking target 3 times in a row   
    private static final AnimatedAction kick_1 = new AnimatedAction(36,12,"kick_1");
    //Sends a wave of hp-draining(hard) butterflies at target
    private static final AnimatedAction butterfly = new AnimatedAction(55,5,"butterfly");
    //Shockwave kind of attack surrounding ambrosia
    private static final AnimatedAction wave = new AnimatedAction(75,5,"wave");
    //Sleep balls
    private static final AnimatedAction sleep = new AnimatedAction(15,7,"sleep");
    //2 spinning kick changing direction between them. also scatters earth damage pollen while doing it
    private static final AnimatedAction kick_2 = new AnimatedAction(36,12,"kick_2");
    
	private static final AnimatedAction[] anims = new AnimatedAction[] {kick_1, butterfly, wave, sleep, kick_2};
	
    public EntityAmbrosia(World world) {
        super(world);
        this.tasks.addTask(1, new EntityAIAmbrosia(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.29);
    }
    
    @Override
    public float attackChance() {
        return 1;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (this.getAnimation()==null || !this.getAnimation().getID().equals("wave")) && super.attackEntityFrom(source, amount);
    }
    
    public void summonButterfly(double x, double y, double z) {
        for (int i = 0; i < 2; ++i) {
            if (!this.world.isRemote) {
                EntityButterfly fly = new EntityButterfly(this.world, (EntityLivingBase)this);
                fly.setPosition(fly.posX+this.rand.nextFloat()*2-1, fly.posY+2+this.rand.nextFloat()*0.5-0.25, fly.posZ+this.rand.nextFloat()*2-1);
                fly.shootAtPosition(x, y, z, 0.3f, 10.0f);
                this.world.spawnEntity(fly);
            }
        }
    }
    
    public void summonWave(int duration) {
        if (!this.world.isRemote) {
            EntityAmbrosiaWave wave = new EntityAmbrosiaWave(this.world, this, duration);
            this.world.spawnEntity(wave);
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
                this.world.spawnEntity(wave);
            }
        }
    }
    
    @Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
    }
    
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
    
	@Override
	public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
		if(type==AnimationType.GENERICATTACK)
			return !anim.getID().equals("kick_2") || this.isEnraged();
		return true;
	}
	
	@Override
	public int animationCooldown(AnimatedAction anim)
	{
		return 34 + this.getRNG().nextInt(22)-(this.isEnraged()?10:0);
	}
}
