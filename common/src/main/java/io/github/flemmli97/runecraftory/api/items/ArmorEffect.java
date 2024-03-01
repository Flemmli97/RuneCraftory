package io.github.flemmli97.runecraftory.api.items;

import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorEffect extends CustomRegistryEntry<ArmorEffect> {

    public boolean canBeAppliedTo(ItemStack stack) {
        return true;
    }

    public void onStep(LivingEntity entity, ItemStack stack) {

    }

    public void onTick(LivingEntity entity, ItemStack stack) {

    }

    public void onEquip(LivingEntity entity, ItemStack stack) {

    }

    public void onRemove(LivingEntity entity, ItemStack stack) {

    }
}
