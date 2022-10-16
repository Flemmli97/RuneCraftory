package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBaseMob {

    LevelExpPair level();

    void setLevel(int level);

    int friendPoints(Player player);

    int baseXP();

    int baseMoney();

    boolean applyFoodEffect(ItemStack stack);

    void removeFoodEffect();
}
