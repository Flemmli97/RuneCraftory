package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;

public class ContainerUpgrade extends Container
{
    public ContainerUpgrade(final IInventory playerInv, final IInventory tileInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        this.addSlotToContainer(new Slot(tileInventory, 8, 56, 35));
        this.addSlotToContainer(new Slot(tileInventory, 7, 20, 35) {
            @Override
			public boolean isItemValid(final ItemStack stack) {
                return tileInventory.isItemValidForSlot(7, stack);
            }
            
            @Override
			public int getSlotStackLimit() {
                return 1;
            }
        });
    }
    
    @Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
        return true;
    }
    
    @Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        final ItemStack itemstack = ItemStack.EMPTY;
        return itemstack;
    }
}
