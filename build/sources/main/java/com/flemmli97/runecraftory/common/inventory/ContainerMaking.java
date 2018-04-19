package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMaking extends Container{

	public ContainerMaking(IInventory playerInv, IInventory tileInventory)
    {
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        for(int i = 0; i < 3; i++)
			this.addSlotToContainer(new Slot(tileInventory, i+1, 20+i*18, 26));
		for(int i = 0; i < 3; i++)
			this.addSlotToContainer(new Slot(tileInventory, i+4, 20+i*18, 44));
        this.addSlotToContainer(new Slot(tileInventory, 0, 83, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return tileInventory.isItemValidForSlot(0, stack);
			}

			@Override
			public int getSlotStackLimit() {
				return 1;
			}
        });
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        return itemstack;
	}	
	
}
