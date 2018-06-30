package com.flemmli97.runecraftory.api.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ItemProperties
{
    private String name;
    private Item item;
    private int meta;
    
    public ItemProperties(String itemName, int meta) 
    {
        this.name = itemName;
        this.meta = meta;
    }
    
    public ItemProperties(Item item) 
    {
        this(item.getRegistryName().toString(), OreDictionary.WILDCARD_VALUE);
    }
    
    public ItemProperties(ItemStack stack) 
    {
        this(stack, true);
    }
    
    public ItemProperties(ItemStack stack, boolean ignoreMeta) 
    {
        this.name = stack.getItem().getRegistryName().toString();
        this.item = stack.getItem();
        this.meta = (ignoreMeta ? OreDictionary.WILDCARD_VALUE : stack.getMetadata());
    }
    
    public ItemStack getItemStack() 
    {
    	//Cache the item, so we dont have to look it up always
        if (this.item == null) 
        {
            this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.name));
        }
        if (this.item != null) 
        {
            return new ItemStack(this.item, 1, (this.meta != OreDictionary.WILDCARD_VALUE) ? this.meta : 0);
        }
        return ItemStack.EMPTY;
    }
    
    public String getItemRegName() {
        return this.name;
    }
    
    public int meta() {
        return this.meta;
    }
    
    @Override
    public String toString() {
        return this.name + ((this.meta != OreDictionary.WILDCARD_VALUE) ? ("," + this.meta) : "");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        {
            return true;
        }
        if (obj instanceof ItemProperties) 
        {
            ItemProperties prop = (ItemProperties)obj;
            return prop.toString().equals(this.toString());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
