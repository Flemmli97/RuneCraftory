package com.flemmli97.runecraftory.common.entity.monster.boss;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.network.PacketAttackDebug;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAmbrosia extends EntityBossBase
{    
    //Tries kicking target 3 times in a row   
    private static final AnimatedAction kick_1 = new AnimatedAction(12,6,"kick_1");
    private static final AnimatedAction kick_2 = new AnimatedAction(12,6,"kick_2");
    private static final AnimatedAction kick_3 = new AnimatedAction(16,6,"kick_3");

    //Sends a wave of hp-draining(hard) butterflies at target
    private static final AnimatedAction butterfly = new AnimatedAction(41,5,"butterfly");
    //Shockwave kind of attack surrounding ambrosia
    private static final AnimatedAction wave = new AnimatedAction(45,5,"wave");
    //Sleep balls
    private static final AnimatedAction sleep = new AnimatedAction(15,5,"sleep");
    //2 spinning changing direction between them. also scatters earth damage pollen while doing it
    private static final AnimatedAction pollen = new AnimatedAction(15,5,"pollen");
    private static final AnimatedAction defeat = new AnimatedAction(204,150,"defeat");
    private static final AnimatedAction angry = new AnimatedAction(48,0,"angry");

    private static final AnimatedAction[] anims = new AnimatedAction[] {kick_1, butterfly, wave, sleep, pollen, kick_2, kick_3, defeat, angry};

	private double[] aiVarHelper;

    public EntityAmbrosia(World world) {
        super(world);
        this.tasks.addTask(1, new EntityAIAmbrosia(this));
        this.setSize(0.85f, 2.3f);

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
        return (this.getAnimation()==null || !(this.getAnimation().getID().equals("wave") || this.getAnimation().getID().equals("angry"))) && super.attackEntityFrom(source, amount);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if(flag && !load)
            this.setAnimation(angry);
    }

    public void setAiVarHelper(double[] aiVarHelper){
        this.aiVarHelper = aiVarHelper;
    }
    
    public void summonButterfly(double x, double y, double z) {
        for (int i = 0; i < 2; ++i) {
            if (!this.world.isRemote) {
                EntityButterfly fly = new EntityButterfly(this.world, this);
                fly.setPosition(fly.posX+this.rand.nextFloat()*2-1, fly.posY+this.rand.nextFloat()*0.5-0.25, fly.posZ+this.rand.nextFloat()*2-1);
                fly.shootAtPosition(x, y, z, 0.3f, 5.0f);
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
    public void handleAttack(AnimatedAction anim) {
        EntityLivingBase target = this.getAttackTarget();
        double distanceToTargetSq = 4;
        if(target!=null) {
            distanceToTargetSq = this.getDistanceSq(target);
            if(!anim.getID().equals("pollen"))
                this.getLookHelper().setLookPositionWithEntity(target, 30.0f, 30.0f);
        }
        switch (anim.getID()) {
            case "butterfly":
                if (anim.getTick() > anim.getAttackTime()) {
                    float yDec = -1.5f;
                    if(distanceToTargetSq<9)
                        yDec=1;
                    else if(distanceToTargetSq<25)
                        yDec=0;
                    this.summonButterfly(this.aiVarHelper[0], this.aiVarHelper[1]-yDec, this.aiVarHelper[2]);
                }
                break;
            case "kick_1":
            case "kick_2":
            case "kick_3":
                if(target!=null) {
                    this.getNavigator().tryMoveToEntityLiving(target, 1.0);
                }
                if (anim.canAttack() && distanceToTargetSq <= 5) {
                    AxisAlignedBB aabb = this.calculateAttackAABB(anim, target);
                    this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.attackPred).forEach(this::attackEntityAsMob);
                    PacketHandler.sendToAll(new PacketAttackDebug(aabb));
                }
                break;
            case "sleep":
                this.getNavigator().clearPath();
                if (anim.canAttack())
                    this.summonSleepBalls();
                break;
            case "wave":
                this.getNavigator().clearPath();
                if (anim.canAttack())
                    this.summonWave(anim.getLength() - anim.getAttackTime());
                break;
            case "pollen":
                if(this.aiVarHelper==null)
                    return;
                this.motionX = this.aiVarHelper[0];
                this.motionZ = this.aiVarHelper[2];
                if (anim.canAttack()) {
                    this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.calculateAttackAABB(anim, target), this.attackPred).forEach(this::attackEntityAsMob);
                }
                break;
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if(this.getAnimation()!=null && this.getAnimation().getID().equals("pollen"))
            return;
        super.applyEntityCollision(entityIn);
    }

    @Override
    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, EntityLivingBase target){
        if(anim.getID().equals("pollen")){
            return this.getEntityBoundingBox().grow(2.0);
        }
        double reach = this.maxAttackRange(anim)*0.5 + this.width*0.5;
        Vec3d dir;
        if(target!=null) {
            reach = Math.min(reach, this.getDistance(target));
            dir = target.getPositionVector().subtract(this.getPositionVector()).normalize();
        }
        else {
            dir = Vec3d.fromPitchYaw(this.rotationPitch, this.rotationYaw);
        }
        Vec3d attackPos = this.getPositionVector().add(dir.scale(reach));
        return this.attackAABB(anim).offset(attackPos.x, this.posY, attackPos.z);
    }
    
    @Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
    }
    
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}

    @Override
    protected void playDeathAnimation(){
        this.setAnimation(defeat);
    }

	@Override
	public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.GENERICATTACK)
            return this.isEnraged() || !anim.getID().equals(pollen.getID());
        return false;
	}

	public AnimatedAction chainAnim(String prev){
        switch(prev){
            case "kick_1": return kick_2;
            case "kick_2": return kick_3;
            case "pollen": return pollen;
        }
        return null;
    }

	@Override
	public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if(anim!=null)
            switch(anim.getID()) {
                case "kick_1":
                case "pollen":
                case "kick_2": return 3;
            }
	    return 54 + this.getRNG().nextInt(22)-(this.isEnraged()?25:0) + diffAdd;
	}
}
