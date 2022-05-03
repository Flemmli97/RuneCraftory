package io.github.flemmli97.runecraftory.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ExtendedItem {

    String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);

    boolean onEntitySwing(ItemStack stack, LivingEntity entity);
}
