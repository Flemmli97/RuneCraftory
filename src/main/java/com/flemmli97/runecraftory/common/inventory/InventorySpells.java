package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.common.items.special.ItemCast;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventorySpells implements IInventory{

	private NonNullList<ItemStack> inv = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY)	;
	
	@Override
	public String getName() {
		return "container.spells";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index<4) {
		ItemStack stack = this.inv.get(index);
		return stack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.removeStackFromSlot(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.inv.get(index);
		this.inv.remove(index);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(this.inv.get(index)!=ItemStack.EMPTY)
			this.inv.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(stack!=null && stack.getItem() instanceof ItemCast || stack.getItem() instanceof ItemSword)
			return true;
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inv.clear();
	}
	
	public void readFromNBT(NBTTagList nbtTagListIn)
    {
		for (int i = 0; i < nbtTagListIn.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(nbttagcompound);
            if(stack!=ItemStack.EMPTY)
            		if(i>=0 && i < this.inv.size())
            			this.inv.set(i, stack);
        }
    }

	public NBTTagList writeToNBT(NBTTagList nbtTagListIn)
    {
		for(int slot = 0; slot< this.inv.size();slot++)
		{
			NBTTagCompound compound = new NBTTagCompound();
			this.inv.get(slot).writeToNBT(compound);
			nbtTagListIn.appendTag(compound);
		}
		return nbtTagListIn;
    }
}
