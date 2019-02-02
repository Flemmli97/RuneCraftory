package com.flemmli97.runecraftory.common.entity.monster.projectile;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityButterfly extends EntityThrowable
{
    private int livingTick;
    
    public EntityButterfly(final World worldIn) {
        super(worldIn);
        this.setSize(0.2f, 0.2f);
    }
    
    public EntityButterfly(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityButterfly(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    public void onUpdate() {
        ++this.livingTick;
        if (this.livingTick > 100) {
            this.setDead();
        }
        super.onUpdate();
    }
    
    protected void onImpact(final RayTraceResult result) {
        if (result.entityHit != null && !this.world.isRemote && result.entityHit != this.getThrower() && result.entityHit instanceof EntityLivingBase) {
            if (RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getThrower(), EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 10), RFCalculations.getAttributeValue(this.getThrower(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {}
            ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 60, 3));
            this.setDead();
        }
    }
    
    protected float getGravityVelocity() {
        return 0.0f;
    }
}
