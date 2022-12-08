package io.github.flemmli97.runecraftory.common.items.equipment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ItemStatShield extends ShieldItem {

    public ItemStatShield(Properties properties) {
        super(properties);
    }

    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }
}
