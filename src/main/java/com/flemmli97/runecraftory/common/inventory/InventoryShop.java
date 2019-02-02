package com.flemmli97.runecraftory.common.inventory;

import java.util.List;

import com.flemmli97.runecraftory.api.entities.IShop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryShop implements IInventory
{
    private NonNullList<ItemStack> slots = NonNullList.withSize(25, ItemStack.EMPTY);
    private IShop shop;
    private int index;
    private ItemStack output = ItemStack.EMPTY;
    
    public InventoryShop(IShop shop) 
    {
        this.shop = shop;
        this.updateInv();
    }
    
    @Override
    public String getName() 
    {
        return "shop";
    }

    @Override
    public boolean hasCustomName() 
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() 
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    @Override
    public int getSizeInventory() 
    {
        return this.slots.size() + 1;
    }

    @Override
    public boolean isEmpty() 
    {
        if (!this.output.isEmpty()) 
            return false;
        for (ItemStack itemstack : this.slots)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) 
    {
        if (index == 25) {
            return this.output;
        }
        return this.slots.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) 
    {
        if (index == 25) {
            return this.output.splitStack(count);
        }
        return ItemStackHelper.getAndSplit(this.slots, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) 
    {
        if (index == 25) {
            ItemStack stack = this.output.copy();
            this.output = ItemStack.EMPTY;
            return stack;
        }
        return ItemStackHelper.getAndRemove(this.slots, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) 
    {
        if (index == 25) {
            this.output = stack;
        }
        else {
            stack.setCount(this.getInventoryStackLimit());
            this.slots.set(index, stack);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        this.updateInv();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }
    
    public void next() {
        this.index = Math.min(this.shop.shopItems().size() / 25, ++this.index);
        this.updateInv();
    }
    
    public void prev() {
        this.index = Math.max(0, --this.index);
        this.updateInv();
    }
    
    private void updateInv() {
        List<ItemStack> list = (List<ItemStack>)this.shop.shopItems();
        for (int i = 0; i < this.slots.size(); ++i) {
            int slot = i + 25 * this.index;
            ItemStack stack = ItemStack.EMPTY;
            if (slot < list.size()) {
                stack = list.get(slot);
            }
            this.slots.set(i, stack);
        }
    }
    
    @Override
    public void clear() {
        this.slots.clear();
    }
}
