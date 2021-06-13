package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DummyInventory extends RecipeWrapper {

    public DummyInventory(IItemHandlerModifiable inv) {
        super(inv);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }
}
