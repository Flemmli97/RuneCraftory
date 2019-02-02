package com.flemmli97.runecraftory.common.blocks.tile;

import java.util.Collection;

import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

public abstract class TileMultiBase extends TileEntity implements IInventory{

    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
    private EnumCrafting type;
    public TileMultiBase(EnumCrafting type)
    {
    		this.type=type;
    }
	@Override
	public boolean hasCustomName() {
		return false;
	}

	public EnumCrafting type()
	{
		return this.type;
	}
	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : inventory)
			if(!stack.isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index<0 || index> this.inventory.size())
			return ItemStack.EMPTY;
		return this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return this.inventory.set(index, ItemStack.EMPTY);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.markDirty();
        }		
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index==0)
			if(stack.getItem() instanceof ItemRecipe && stack.getMetadata()==this.type.getID())
				return true;
			else
				return false;
		else if(index==7)
			if(validItem(stack))
				return true;
			else 
				return false;
		return true;
	}
	
	protected abstract boolean validItem(ItemStack stack);

	@Override
	public int getField(int id) {return 0;}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {return 0;}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, this.inventory);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.inventory);
		return compound;
	}
	
	public void tryCrafting(EntityPlayer player)
	{
		ItemStack stack = this.inventory.get(0);
		if(stack.getItem() instanceof ItemRecipe && stack.hasTagCompound())
		{
			Collection<RecipeSextuple> recipes = CraftingHandler.getRecipeFromID(this.type, stack.getTagCompound().getString("Recipe"));
			//TODO: add crafting bonus
			for(RecipeSextuple recipe : recipes)
				if(recipe.doesRecipeMatch(this.inventory.subList(1, 7)))
				{
					ItemUtils.spawnItemAtEntity(player, recipe.getCraftingOutput());
					for(int i = 1; i < 7; i++)
					{
						this.inventory.get(i).shrink(1);
					}
					this.markDirty();
					return;
				}
		}
	}
	
	public void tryUpgrade(EntityPlayer player)
	{
		ItemStack upgrade = this.inventory.get(8);
		ItemStack toUpgrade = this.inventory.get(7);
		if(toUpgrade.getItem() instanceof IItemWearable)
		{
			if(ItemNBT.addItemLevel(toUpgrade))
			{
				ItemNBT.addUpgradeItem(toUpgrade, upgrade);
				if(upgrade.getItem()==ModItems.crystalWater)
					ItemNBT.setElement(EnumElement.WATER, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalEarth)
					ItemNBT.setElement(EnumElement.EARTH, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalFire)
					ItemNBT.setElement(EnumElement.FIRE, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalWind)
					ItemNBT.setElement(EnumElement.WIND, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalLight)
					ItemNBT.setElement(EnumElement.LIGHT, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalDark)
					ItemNBT.setElement(EnumElement.DARK, toUpgrade);
				else if(upgrade.getItem()==ModItems.crystalLove)
					ItemNBT.setElement(EnumElement.LOVE, toUpgrade);
				this.inventory.get(8).shrink(1);
				player.world.playSound(null, player.getPosition(),SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
			}
			else
				player.world.playSound(null, player.getPosition(),SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
			this.markDirty();
		}
	}

}
