package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStrings extends Item implements IItemBase{
	
	private String[] strings = new String[] {"yarn", "bandage", "ambrosia","spider","puppet","vine","tail","strine","pretty","chimera"};

	public ItemStrings()
	{
		super();
		this.setCreativeTab(RuneCraftory.upgradeItems);
        	this.setRegistryName(new ResourceLocation(LibReference.MODID, "strings" ));	
        	this.setUnlocalizedName(this.getRegistryName().toString());
        	this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_"+ this.strings[stack.getMetadata()];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.upgradeItems)
		for (int i = 0; i < 10; i ++) {
			items.add(new ItemStack(this, 1, i));
	    }
	}
	@Override
	public int getBuyPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getSellPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getUpgradeDifficulty() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{	
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 10;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+"_"+strings[meta], "inventory"));		
	}

}
