package io.github.flemmli97.runecraftory.api.datapack;

import net.minecraft.world.item.ItemStack;

public record ShopItemProperties(ItemStack stack, boolean needsSpecialUnlocking) {
}
