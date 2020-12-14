package com.flemmli97.runecraftory.api.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpells
{
    void levelSkill(PlayerEntity player);

    int coolDown();

    boolean use(World world, PlayerEntity player, ItemStack stack);

    void update(ItemStack stack, PlayerEntity player);
}
