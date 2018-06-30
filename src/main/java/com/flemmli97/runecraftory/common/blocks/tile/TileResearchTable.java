package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.blocks.IHerb;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.misc.ItemMinerals;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.oredict.OreDictionary;

public class TileResearchTable extends TileEntity implements IInventory{

    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	@Override
	public String getName() {
		return "tile.research";
	}

	@Override
	public boolean hasCustomName() {
		return false;
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
		if(index==1)
			if(stack.getItem()==Items.BREAD)
				return true;
			else 
				return false;
		if(index == 2)
			for(ItemStack ore : OreDictionary.getOres("paper"))
				if(OreDictionary.itemMatches(stack, ore, false))
					return true;
			else
				return false;
		if(index==3)
			return false;
		return true;
	}

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
	
	public void recipe(EntityPlayer player, int levelMin, int levelMax)
	{
		boolean hasPaper = false;
		IPlayer capSync = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);

		for(ItemStack stack : OreDictionary.getOres("paper"))
		{
			if(OreDictionary.itemMatches(this.inventory.get(2), stack, false))
			{
				hasPaper=true;
				break;
			}
		}
		if(this.inventory.get(1).getItem()==Items.BREAD && hasPaper && this.inventory.get(3).isEmpty())
		{
			
			for(ItemStack stack : OreDictionary.getOres("gemDiamond"))
			{
				if(OreDictionary.itemMatches(this.inventory.get(0), stack, false))
				{
					if(capSync.getSkillLevel(EnumSkills.FORGING)[0]>levelMax-10)
						this.getRandomRecipe(player, EnumCrafting.FORGE, levelMin, levelMax);
					else
					{
						player.world.playSound(null, getPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
						player.world.playSound(null, getPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
					}
					return;
				}
			}
			if(this.inventory.get(0).getItem() instanceof ItemMinerals)
				if(capSync.getSkillLevel(EnumSkills.CRAFTING)[0]>levelMax-10)
				;//this.getRandomRecipe(player, EnumCrafting.ARMOR, levelMin, levelMax);
				else
					player.world.playSound(null, getPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
			else if(this.inventory.get(0).getItem() instanceof ItemFood && ((ItemFood)this.inventory.get(0).getItem()).getHealAmount(this.inventory.get(0))>=3)
				if(capSync.getSkillLevel(EnumSkills.COOKING)[0]>levelMax-10)
				;//this.getRandomRecipe(player, EnumCrafting.COOKING, levelMin, levelMax);
				else
					player.world.playSound(null, getPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
			else if(this.inventory.get(0).getItem() instanceof ItemBlock && ((ItemBlock)this.inventory.get(0).getItem()).getBlock() instanceof IHerb)
				if(capSync.getSkillLevel(EnumSkills.CHEMISTRY)[0]>levelMax-10)
				;//this.getRandomRecipe(player, EnumCrafting.PHARMA, levelMin, levelMax);
				else
					player.world.playSound(null, getPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1, 1);
		}
	}
	
	private void getRandomRecipe(EntityPlayer player, EnumCrafting type, int levelMin, int levelMax)
	{
		for(ItemStack stack : this.inventory)
			stack.shrink(1);
		ItemStack stack = new ItemStack(ModItems.recipe);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("Recipe", CraftingHandler.randomRecipeToExclude(type,levelMin, levelMax));
		stack.setItemDamage(type.getID());
		this.inventory.set(3, stack);
		for(int i = 0; i < this.inventory.size()-1; i++)
			this.inventory.get(i).shrink(1);
		this.markDirty();
	}
}
