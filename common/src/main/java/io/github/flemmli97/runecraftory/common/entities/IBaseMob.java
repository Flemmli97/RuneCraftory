package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.world.item.ItemStack;

public interface IBaseMob {

    int level();

    void setLevel(int level);

    int baseXP();

    int baseMoney();

    boolean applyFoodEffect(ItemStack stack);

    void removeFoodEffect();
}
