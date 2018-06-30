package com.flemmli97.runecraftory.common.potion;

import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionPoison extends PotionPermanent
{
    public PotionPoison() {
        super("rfPoison", 0);
        this.setTickDelay(60);
    }
    
    @Override
    public void performEffect(EntityLivingBase livingbase, int amplifier) {
        if (livingbase instanceof EntityPlayer) {
            IPlayer cap = livingbase.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
            float amount = cap.getMaxHealth() * 0.05f;
            amount = ((cap.getHealth() - amount <= 0.0f) ? (cap.getHealth() - 1.0f) : amount);
            cap.damage((EntityPlayer)livingbase, CustomDamage.EXHAUST, amount);
        }
        super.performEffect(livingbase, amplifier);
    }
}
