package com.flemmli97.runecraftory.common.potion;

import com.flemmli97.runecraftory.common.core.network.PacketDisableInteraction;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionSleep extends Potion{
	
	public PotionSleep() {
		super(true, 0);
		this.setPotionName("sleep");
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "sleep"));
	}

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        if(entityLivingBaseIn instanceof EntityLiving)
        		((EntityLiving)entityLivingBaseIn).setNoAI(false);        	
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier)
    {
    		if(entity instanceof EntityLiving)
    			((EntityLiving)entity).setNoAI(true);
        else if(entity instanceof EntityPlayer && !entity.world.isRemote)
        {
        		PacketHandler.sendTo(new PacketDisableInteraction(), (EntityPlayerMP) entity);
        }super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
    }
}
