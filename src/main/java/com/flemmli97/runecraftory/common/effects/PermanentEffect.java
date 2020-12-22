package com.flemmli97.runecraftory.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class PermanentEffect extends Effect {

    private int tickDelay;

    public PermanentEffect(EffectType type, int color) {
        super(type, color);
        this.tickDelay = 5;
    }

    public PermanentEffect setTickDelay(int value) {
        this.tickDelay = value;
        return this;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % this.tickDelay == 0;
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        living.removePotionEffect(this);
        living.addPotionEffect(new EffectInstance(this, 2 * this.tickDelay - 1));
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
