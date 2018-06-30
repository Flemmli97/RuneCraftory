package com.flemmli97.runecraftory.common.potion;

import com.flemmli97.runecraftory.common.entity.ai.EntityAIDisable;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionSleep extends Potion
{
    public PotionSleep() {
        super(true, 0);
        this.setPotionName("sleep");
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "sleep"));
    }
    
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
    
    public void performEffect(EntityLivingBase ent, int amplifier) {
    	if(!(ent instanceof EntityPlayer) || !((EntityPlayer)ent).capabilities.disableDamage)
    		ent.motionY -= 0.08;
    }
    
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
        if (entity instanceof EntityLiving) {
            ((EntityLiving)entity).tasks.addTask(0, (EntityAIBase)new EntityAIDisable((EntityLiving)entity));
        }
        super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
    }
}
