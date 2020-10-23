package com.flemmli97.runecraftory.mobs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Map;
import java.util.UUID;

public interface IExtendedMob extends IBaseMob {

    NonNullList<ItemStack> tamingItem();

    Map<ItemStack, Integer> dailyDrops();

    float tamingChance();

    boolean isTamed();

    boolean ridable();

    UUID ownerUUID();

    PlayerEntity getOwner();

    void setOwner(PlayerEntity player);

    boolean isFlyingEntity();

    float attackChance();
}
