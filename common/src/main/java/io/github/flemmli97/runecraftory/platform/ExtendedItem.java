package io.github.flemmli97.runecraftory.platform;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ExtendedItem {

    boolean onEntitySwing(ItemStack stack, LivingEntity entity);
}
