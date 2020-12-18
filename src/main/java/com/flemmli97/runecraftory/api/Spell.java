package com.flemmli97.runecraftory.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    public abstract void update(PlayerEntity player, ItemStack stack);

    public abstract void levelSkill(PlayerEntity player);

    public abstract int coolDown();

    public abstract boolean use(World world, LivingEntity player, ItemStack stack);
}
