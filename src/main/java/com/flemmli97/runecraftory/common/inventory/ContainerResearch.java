package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;

public class ContainerResearch extends Container
{
    public ContainerResearch(IInventory playerInv, IInventory tileInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        this.addSlotToContainer((Slot)new Slot(tileInventory, 0, 38, 17) {
            public boolean isItemValid(ItemStack stack) {
                return tileInventory.isItemValidForSlot(0, stack);
            }
        });
        this.addSlotToContainer((Slot)new Slot(tileInventory, 1, 20, 35) {
            public boolean isItemValid(ItemStack stack) {
                return tileInventory.isItemValidForSlot(1, stack);
            }
        });
        this.addSlotToContainer((Slot)new Slot(tileInventory, 2, 56, 35) {
            public boolean isItemValid(ItemStack stack) {
                return tileInventory.isItemValidForSlot(2, stack);
            }
        });
        this.addSlotToContainer((Slot)new Slot(tileInventory, 3, 116, 35) {
            public boolean isItemValid(ItemStack stack) {
                return tileInventory.isItemValidForSlot(3, stack);
            }
        });
    }
    
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
    
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        return itemstack;
    }
}
