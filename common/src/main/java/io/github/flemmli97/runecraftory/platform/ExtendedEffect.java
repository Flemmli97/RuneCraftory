package io.github.flemmli97.runecraftory.platform;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ExtendedEffect {

    List<ItemStack> getCurativeItems();

    default boolean renderIcons() {
        return false;
    }
}
