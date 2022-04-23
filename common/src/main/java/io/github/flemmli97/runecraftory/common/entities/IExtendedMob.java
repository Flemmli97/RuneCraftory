package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.config.ItemTagWrapper;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface IExtendedMob extends IBaseMob, OwnableEntity {

    ItemTagWrapper tamingItem();

    Map<ItemStack, Integer> dailyDrops();

    float tamingChance();

    boolean isTamed();

    boolean ridable();

    void setOwner(Player player);

    boolean isFlyingEntity();

    float attackChance(AnimationType type);
}
