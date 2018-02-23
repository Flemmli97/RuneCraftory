package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecipe extends Item{

	public ItemRecipe()
	{
		super();
        	this.setRegistryName(new ResourceLocation(LibReference.MODID, "recipe" ));	
        	this.setUnlocalizedName(this.getRegistryName().toString());
        	this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName();
	}
	
	public void setRecipe(ItemStack stack, String recipeID)
	{
		if(!stack.hasTagCompound())
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("Recipe", recipeID);
			stack.setTagCompound(compound);
		}
		else
			stack.getTagCompound().setString("Recipe", recipeID);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 4;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+"_"+meta, "inventory"));		
	}
}
