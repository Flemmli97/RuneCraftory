package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMinerals extends Item implements IItemBase, IModelRegister{
	
	private String[] minerals = new String[] {"iron","bronze","silver","gold","platinum","orichalcum","dragonic"};

	public ItemMinerals()
	{
		super();
		this.setCreativeTab(RuneCraftory.upgradeItems);
        	this.setRegistryName(new ResourceLocation(LibReference.MODID, "mineral" ));	
        	this.setUnlocalizedName(this.getRegistryName().toString());
        	this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		if(meta<this.minerals.length)
			return this.getUnlocalizedName() + "_"+ this.minerals[stack.getMetadata()];
		return this.getUnlocalizedName() + "_"+ this.minerals[0];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.upgradeItems)
		for (int i = 0; i < 7; i ++) {
			ItemStack stack = new ItemStack(this, 1, i);
			ItemNBT.initItemNBT(stack, this.defaultNBTStats(stack));
            items.add(stack);
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
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagList emtpyUpgrade = new NBTTagList();

		switch(stack.getMetadata())
		{
			case 0:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 1);
				break;
			case 1:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 4);

				break;
			case 2:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 7);
				break;
			case 3:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 10);

				break;
			case 4:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 25);
				break;
			case 5:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 95);
				break;
			case 6:
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 160);
				stats.setInteger(ItemStats.RFMAGICDEF.getName(), 120);
				break;
		}
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 7;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+"_"+minerals[meta], "inventory"));		
	}

}
