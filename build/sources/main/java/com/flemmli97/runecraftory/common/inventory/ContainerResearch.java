package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerResearch extends Container{

	public ContainerResearch(IInventory playerInv, IInventory tileInventory)
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
        
		this.addSlotToContainer(new Slot(tileInventory, 0, 38, 17){
			@Override
			public boolean isItemValid(ItemStack stack) {
				return tileInventory.isItemValidForSlot(0, stack);
			}
        });
        this.addSlotToContainer(new Slot(tileInventory, 1, 20, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return tileInventory.isItemValidForSlot(1, stack);
			}
        });
        this.addSlotToContainer(new Slot(tileInventory, 2, 56, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return tileInventory.isItemValidForSlot(2, stack);
			}
        });
        this.addSlotToContainer(new Slot(tileInventory, 3, 116, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return tileInventory.isItemValidForSlot(3, stack);
			}
        });
    }
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}	
}
