package com.flemmli97.runecraftory.common.entity.magic;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFireBall extends EntityProjectile
{
    public EntityFireBall(World worldIn) {
        super(worldIn);
    }
    
    public EntityFireBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityFireBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    @Override
	protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null) {
            RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getShooter(), EnumElement.FIRE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 5), RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null));
            this.world.playSound(null, result.entityHit.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.setDead();
        }
        else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.setDead();
        }
    }
    
    @Override
	protected float getGravityVelocity() {
        return 0.005f;
    }
}
