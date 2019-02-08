package com.flemmli97.runecraftory.common.entity.magic;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.entity.EntityBeam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWaterLaser extends EntityBeam{

	public static final int range = 16;
	public static final int livingTickMax = 60;

	public EntityWaterLaser(World world) {
		super(world);
	}
	
	public EntityWaterLaser(World world, EntityLivingBase shooter)
    {
		super(world, shooter);
    }
	
	@Override
	public boolean piercing()
	{
		return true;
	}
	
	@Override
    public float getRange()
    {
    	return range;
    }
	
	@Override
	public int livingTickMax()
	{
		return livingTickMax;
	}
	
	@Override
	public boolean getHitVecFromShooter()
	{
		return this.getShooter() instanceof EntityPlayer;
	}
	
	@Override
	public int attackCooldown()
	{
		return 0;
	}

	@Override
	protected void onImpact(RayTraceResult result) 
	{
        RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getShooter(), EnumElement.WATER, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 5), RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null));
        result.entityHit.hurtResistantTime=19;
	}

}
