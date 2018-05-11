package com.flemmli97.runecraftory.common.entity.magic;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFireBall extends EntityThrowable{

	public EntityFireBall(World worldIn)
    {
        super(worldIn);
    }

    public EntityFireBall(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityFireBall(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.typeOfHit==RayTraceResult.Type.ENTITY && result.entityHit!=null)
		{
			if(!this.world.isRemote)
			{
				float dmg = 0;
				if(this.getThrower() instanceof EntityPlayer)
				{
					IPlayer cap = this.getThrower().getCapability(PlayerCapProvider.PlayerCap, null);
					dmg+=cap.getIntel();
				}
				RFCalculations.attackEntity(result.entityHit, 
						CustomDamage.attack(this.getThrower(), EnumElement.FIRE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0, 5), 
						dmg+RFCalculations.getAttributeValue(this.getThrower(), ItemStats.RFMAGICATT, null, null));
			}
			this.world.playSound(null, result.entityHit.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
			this.setDead();
		}
		else if(result.typeOfHit==RayTraceResult.Type.BLOCK)
		{
			this.world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
			this.setDead();
		}
	}
	
	@Override
    protected float getGravityVelocity()
    {
        return 0.005F;
    }
}
