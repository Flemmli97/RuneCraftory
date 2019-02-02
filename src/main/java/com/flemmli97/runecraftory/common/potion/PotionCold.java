package com.flemmli97.runecraftory.common.potion;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.lib.LibPotions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionCold extends PotionPermanent
{
    public PotionCold() {
        super(LibPotions.COLD, 0);
        this.setTickDelay(60);
    }
    
    @Override
    public void performEffect(EntityLivingBase livingbase, int amplifier) {
        if (livingbase instanceof EntityPlayer) {
            IPlayer cap = livingbase.getCapability(PlayerCapProvider.PlayerCap, null);
            int amount = Math.min(cap.getRunePoints(), (int)(cap.getMaxRunePoints() * 0.05));
            cap.decreaseRunePoints((EntityPlayer)livingbase, amount);
        }
        super.performEffect(livingbase, amplifier);
    }
}
