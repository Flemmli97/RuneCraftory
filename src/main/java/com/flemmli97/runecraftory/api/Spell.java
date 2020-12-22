package com.flemmli97.runecraftory.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    public abstract void update(PlayerEntity player, ItemStack stack);

    public abstract void levelSkill(ServerPlayerEntity player);

    public abstract int coolDown();

    public abstract boolean use(ServerWorld world, LivingEntity entity, ItemStack stack);

    public abstract int rpCost();
}
