package com.flemmli97.runecraftory.common.entity.monster.boss;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIThunderbolt;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityThunderbolt extends EntityBossBase{

    //Simple kick   
    private static final AnimatedAction front_kick = new AnimatedAction(36,12,"front_kick");
    //Turns and kicks with backlegs
    private static final AnimatedAction back_kick = new AnimatedAction(85,7,"back_kick");
    //Shoots out 5 wind lazer beams in front
    private static final AnimatedAction lazer_x5 = new AnimatedAction(125,5,"lazer_x5");
    //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
    private static final AnimatedAction stomp = new AnimatedAction(12,10,"stomp");
    //Horn attack: throws player into air and then kicks him away
    private static final AnimatedAction horn_attack = new AnimatedAction(36,12,"horn_attack");
    //Charges 2 times at the player. 2-3 when enraged
    private static final AnimatedAction charge = new AnimatedAction(36,12,"charge");
    //Shoots lazers in all directions. only enraged
    private static final AnimatedAction lazer_aoe = new AnimatedAction(36,12,"lazer_aoe");
    //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
    private static final AnimatedAction lazer_kick = new AnimatedAction(36,12,"lazer_kick");
    //Shoots 2 slight homing wind blades
    private static final AnimatedAction wind_blade = new AnimatedAction(36,12,"wind_blade");
    //Used after feinting death
    private static final AnimatedAction lazer_kick_x3 = new AnimatedAction(36,12,"lazer_kick_x3");

    private static final AnimatedAction[] anims = new AnimatedAction[] {front_kick, back_kick, lazer_x5, stomp, horn_attack, wind_blade, charge, lazer_aoe, lazer_kick, lazer_kick_x3};
	
    private static DataParameter<Boolean> death_feinted = EntityDataManager.createKey(EntityThunderbolt.class, DataSerializers.BOOLEAN);;

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
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(death_feinted, false);
    }
    
    public boolean feintedDeath() {
        return this.dataManager.get(death_feinted);
    }
    
    public void feintDeath(boolean flag) {
        this.dataManager.set(death_feinted, flag);
    }

	@Override
	public float attackChance() {
		return 1;
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("DeathFeinted", this.feintedDeath());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.feintDeath(compound.getBoolean("DeathFeinted"));
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

	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
	
	@Override
	public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
		return true;
	}

	@Override
    public int animationCooldown(AnimatedAction anim)
    {
        return 30 + this.getRNG().nextInt(12)-(this.isEnraged()?10:0)-(this.feintedDeath()?5:0);
    }
}
