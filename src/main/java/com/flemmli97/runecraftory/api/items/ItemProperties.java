package com.flemmli97.runecraftory.api.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ItemProperties
{
	private String name;
	//For caching
	private Item item;
	private int meta, amount;

	public ItemProperties(String itemName, int amount, int meta)
	{
		
		this.name=itemName;
		this.amount=amount;
		this.meta=meta;
	}
	
	public ItemStack getItemStack()
	{
		//Cache the item so we dont have to look it up always
		if(this.item==null)
			this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
		if(this.item!=null)
		{
			return new ItemStack(this.item, this.amount, this.meta==-1?OreDictionary.WILDCARD_VALUE:this.meta);
		}
		return ItemStack.EMPTY;
	}
	
	public String getItemRegName()
	{
		return this.name;
	}
	
	public int meta()
	{
		return this.meta;
	}
	
	public int amount()
	{
		return this.amount;
	}
	
	public String toStringNoAmount()
	{
		return this.name+","+this.meta;
	}
	
	@Override
	public String toString()
	{
		return this.name+","+this.meta+","+this.amount;
	}
}
