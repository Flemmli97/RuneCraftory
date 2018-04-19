package com.flemmli97.runecraftory.common.entity.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFireball extends EntityThrowable{

	public EntityFireball(World worldIn)
    {
        super(worldIn);
    }

    public EntityFireball(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityFireball(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.entityHit!=null)
		{
			AxisAlignedBB aabb = new AxisAlignedBB(result.entityHit.posX-0.2, result.entityHit.posY-0.2, result.entityHit.posZ-0.2, result.entityHit.posX+0.2, result.entityHit.posY+0.2, result.entityHit.posZ+0.2);
		}
		else
		{
			
		}
	}

}
