package com.flemmli97.runecraftory.common.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemNBT {

	public static int itemLevel(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound().getInteger("ItemLevel"):1;
	}
	
	public static boolean addItemLevel(ItemStack stack) {		
		if(itemLevel(stack)<10)
		{
			if(stack.hasTagCompound())
			{
				stack.getTagCompound().setInteger("ItemLevel", itemLevel(stack)+1);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getter for the attribute mapping regarding stats.
	 * @param stack
	 * @return A mapping containing the attributes and the value
	 */
	public static Map<ItemStats, Integer> statIncrease(ItemStack stack){
		Map<ItemStats, Integer> map = new LinkedHashMap<ItemStats, Integer>();
		if(stack.hasTagCompound())
		{
			NBTTagCompound tag = (NBTTagCompound) stack.getTagCompound().getTag("ItemStats");
			List<ItemStats> ordered = new LinkedList<ItemStats>();
			for(String attName : tag.getKeySet())
			{
				ordered.add(ItemStats.ATTRIBUTESTRINGMAP.get(attName));
			}
			ordered.sort(new ItemStats.Sort());
			for(ItemStats att : ordered)
			{	 
				map.put(att, tag.getInteger(att.getName()));
			}
		}
		return map;
	}
	
	/**
	 * Adds stats to the item through e.g. forging //TODO replace using the upgrade list
	 */
	public static void updateStatIncrease(ItemStats attribute, int amount, ItemStack stack) {
			int oldValue = ((NBTTagCompound) stack.getTagCompound().getTag("ItemStats")).getInteger(attribute.getName());
			((NBTTagCompound) stack.getTagCompound().getTag("ItemStats")).setInteger(attribute.getName(), oldValue+=amount);
	}

	/**
	 * Sets the items elemental damage
	 */
	public static void setElement(EnumElement element, ItemStack stack)
	{
		if(stack.hasTagCompound())
			if(EnumElement.fromName(stack.getTagCompound().getString("Element"))==EnumElement.NONE)
				stack.getTagCompound().setString("Element", element.getName());
			else
				stack.getTagCompound().setString("Element", EnumElement.NONE.getName());
	}
	
	public static EnumElement getElement(ItemStack stack)
	{
		if(stack.hasTagCompound())
			return EnumElement.fromName(stack.getTagCompound().getString("Element"));
		else
			return EnumElement.NONE;
	}
	
	/**
	 * Gets all Items used in upgrading this item.
	 */
	public static List<ItemStack> upgradeItems(ItemStack stack)
	{
		NBTTagList nbtList= stack.getTagCompound().getTagList("Upgrades", 10);
		List<ItemStack> list = new ArrayList<ItemStack>();
        for (int i = 0; i < nbtList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbtList.getCompoundTagAt(i);
			list.add(new ItemStack(nbttagcompound));
		}
		return list;
	}
	
	/**
	 * Adds an upgrade item to this stack.
	 */
	public static void addUpgradeItem(ItemStack stack, ItemStack stackToAdd) {
		if(stack.hasTagCompound())
		{
			NBTTagList nbtList= stack.getTagCompound().getTagList("Upgrades", 10);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
	        stackToAdd.writeToNBT(nbttagcompound);
	        nbtList.appendTag(nbttagcompound);
	        stack.getTagCompound().setTag("Upgrades", nbtList);
	        if(!(stackToAdd.getItem() instanceof IRpUseItem))
        		{
		        Map<ItemStats, Integer> stats = statIncrease(stackToAdd);
		        for(ItemStats att : stats.keySet())
		        		updateStatIncrease(att, stats.get(att), stack);
        		}
		}
	}
	
	/**
	 * Called to initiate default stats on item on creation.
	 * Things that uses nbt: Element, Level, Upgrades, Stats
	 */
	public static void initItemNBT(ItemStack stack, NBTTagCompound nbt)
	{
		if(nbt!=null)
			if(stack.hasTagCompound())
			{
				NBTTagCompound stackCompound = stack.getTagCompound();
				nbt.merge(stackCompound);
				stack.setTagCompound(nbt);
			}
			else
				stack.setTagCompound(nbt);
	}
}
