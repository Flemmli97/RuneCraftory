package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DummyInventory extends RecipeWrapper {

    public DummyInventory(IItemHandlerModifiable inv) {
        super(inv);
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }
}
