package com.flemmli97.runecraftory.common.items.consumables;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class ItemMedicine extends Item {

    public ItemMedicine(Item.Properties props) {
        super(props);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 3;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
