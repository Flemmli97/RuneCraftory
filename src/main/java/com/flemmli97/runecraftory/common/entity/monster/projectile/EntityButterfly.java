package com.flemmli97.runecraftory.common.entity.monster.projectile;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage.KnockBackType;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityButterfly extends EntityThrowable{

	private int livingTick;
	public EntityButterfly(World worldIn)
    {
        super(worldIn);
        this.setSize(0.2F, 0.2F);
    }

    public EntityButterfly(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityButterfly(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
    
    

    @Override
	public void onUpdate() {
		this.livingTick++;
		if(this.livingTick>100)
			this.setDead();
		super.onUpdate();
	}

	//drain method missing
	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.entityHit!=null && !this.world.isRemote)
		{
			if(result.entityHit!=this.getThrower() && result.entityHit instanceof EntityLivingBase)
			{
				if(RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getThrower(), EnumElement.NONE, CustomDamage.DamageType.NORMAL, KnockBackType.BACK, 0, 10), RFCalculations.getAttributeValue(this.getThrower(), ItemStats.RFMAGICATT, null, null)/6.0F));
					((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 60, 3));
				this.setDead();
			}
		}
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0;
	}
}
