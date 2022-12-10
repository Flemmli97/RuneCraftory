package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IBaseMob {

    LevelExpPair level();

    void setLevel(int level);

    default int friendPoints(Player player) {
        return this.friendPoints(player.getUUID());
    }

    int friendPoints(UUID uuid);

    int baseXP();

    int baseMoney();

    boolean applyFoodEffect(ItemStack stack);

    void removeFoodEffect();

    default boolean onGivingItem(Player player, ItemStack stack) {
        return false;
    }
}
