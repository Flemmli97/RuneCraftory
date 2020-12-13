package com.flemmli97.runecraftory.common.entities;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;

import java.util.Map;

public interface IBaseMob {

    int level();

    void setLevel(int level);

    int baseXP();

    int baseMoney();

    void applyFoodEffect(ItemStack stack);

    void updateClientFoodEffect(Map<Attribute, Integer> stats, Map<Attribute, Float> multi, int duration);

    void removeFoodEffect();
}
