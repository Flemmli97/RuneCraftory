package com.flemmli97.runecraftory.common.potion;

import net.minecraft.util.*;
import net.minecraft.item.*;
import java.util.*;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.*;
import net.minecraft.potion.*;

public class PotionPermanent extends Potion
{
    private int tickDelay;
    
    public PotionPermanent(String name, int color) {
        super(true, color);
        this.tickDelay = 1;
        this.setPotionName(name);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
    }
    
    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<ItemStack>();
    }
    
    protected void setTickDelay(int value) {
        this.tickDelay = value;
    }
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % this.tickDelay == 0;
    }
    @Override
    public void performEffect(EntityLivingBase livingbase, int amplifier) {
        livingbase.removePotionEffect(this);
        livingbase.addPotionEffect(new PotionEffect(this, 2 * this.tickDelay - 1));
    }
    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }
}
