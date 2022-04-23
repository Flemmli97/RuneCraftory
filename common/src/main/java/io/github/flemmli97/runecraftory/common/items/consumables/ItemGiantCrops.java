package io.github.flemmli97.runecraftory.common.items.consumables;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemGiantCrops extends Item {

    public ItemGiantCrops(Item.Properties props) {
        super(props);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return (int) (super.getUseDuration(stack) * 1.5);
    }
}