package io.github.flemmli97.runecraftory.common.entities;

import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Map;

public interface IExtendedMob extends IBaseMob, IOwnable<PlayerEntity> {

    NonNullList<ItemStack> tamingItem();

    Map<ItemStack, Integer> dailyDrops();

    float tamingChance();

    boolean isTamed();

    boolean ridable();

    void setOwner(PlayerEntity player);

    boolean isFlyingEntity();

    float attackChance(AnimationType type);
}
