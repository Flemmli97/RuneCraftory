package com.flemmli97.runecraftory.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    public abstract void update(PlayerEntity player, ItemStack stack);

    public abstract void levelSkill(ServerPlayerEntity player);

    public abstract int coolDown();

    public boolean use(ServerWorld world, LivingEntity entity, @Nullable ItemStack stack) {
        return this.use(world, entity, stack, 1, 1, 1);
    }

    public abstract boolean use(ServerWorld world, LivingEntity entity, @Nullable ItemStack stack, float rpUseMultiplier, int amount, int level);

    public abstract int rpCost();

    @Override
    public String toString() {
        return this.getRegistryName().toString();
    }
}
