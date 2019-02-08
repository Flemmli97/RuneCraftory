package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.api.entities.IShop;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShop extends Container
{
    private InventoryShop invShop;
    private IShop shop;
    
    public ContainerShop(InventoryPlayer playerInventory, IShop shop) {
        this.invShop = new InventoryShop(shop);
        this.shop = shop;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlotToContainer(new Slot(this.invShop, j + i * 5, -8 + j * 27, -4 + i * 21) {
                    @Override
					public boolean isItemValid(ItemStack stack) {
                        return false;
                    }
                    
                    @Override
					public boolean canTakeStack(EntityPlayer playerIn) {
                        return false;
                    }
                });
            }
        }
        this.addSlotToContainer(new Slot(this.invShop, 25, 180, 109) {
            @Override
			public boolean isItemValid(ItemStack stack) {
                return false;
            }
            
            @Override
			public boolean canTakeStack(EntityPlayer playerIn) {
                return false;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, -8 + j * 18, 134 + i * 18 - 25));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, -8 + k * 18, 167));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
    
    public InventoryShop getShopInv() {
        return this.invShop;
    }
    
    public IShop shopInstance() {
        return this.shop;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if (index <= 25) {
            return ItemStack.EMPTY;
        }
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        ItemStack itemstack2 = slot.getStack();
        itemstack = itemstack2.copy();
        if (index < 53) {
            if (!this.mergeItemStack(itemstack2, 53, 62, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (!this.mergeItemStack(itemstack2, 26, 53, false)) {
            return ItemStack.EMPTY;
        }
        if (itemstack2.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        }
        else {
            slot.onSlotChanged();
        }
        if (itemstack2.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }
        return itemstack;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId > 24 || slotId < 0) {
            return super.slotClick(slotId, dragType, clickType, player);
        }
        ItemStack clickedStack = this.inventorySlots.get(slotId).getStack();
        ItemNBT.getItemNBT(clickedStack);
        Slot shopOutput = this.inventorySlots.get(25);
        if (clickType == ClickType.PICKUP || clickType == ClickType.PICKUP_ALL) {
            int count = (clickType == ClickType.PICKUP) ? 1 : 10;
            if (!shopOutput.getHasStack() || !ItemUtils.areItemsStackable(clickedStack, shopOutput.getStack())) {
                ItemStack copy = clickedStack.copy();
                copy.setCount(count);
                shopOutput.putStack(copy);
            }
            else {
                shopOutput.getStack().setCount(Math.min(shopOutput.getStack().getMaxStackSize() * 36, shopOutput.getStack().getCount() + count));
            }
        }
        else if (clickType == ClickType.QUICK_CRAFT || clickType == ClickType.QUICK_MOVE) {
            int count = (clickType == ClickType.QUICK_CRAFT) ? 1 : 10;
            if (shopOutput.getHasStack()) {
                shopOutput.getStack().setCount(shopOutput.getStack().getCount() - count);
                if (shopOutput.getStack().getCount() <= 0) {
                    shopOutput.putStack(ItemStack.EMPTY);
                }
            }
        }
        this.detectAndSendChanges();
        return ItemStack.EMPTY;
    }
    
    public void resetSlot() {
        this.inventorySlots.get(25).putStack(ItemStack.EMPTY);
        this.detectAndSendChanges();
    }
}
