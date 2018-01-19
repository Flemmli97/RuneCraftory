package com.flemmli97.runecraftory.common.items.misc;

import java.util.Map;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IUpgradeItem;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBones extends Item implements IItemBase, IUpgradeItem{
	
	private String[] bones = new String[] {"turtle", "fish", "skull","dragon","tortoise","ammonite"};

	public ItemBones()
	{
		super();
		this.setCreativeTab(RuneCraftory.upgradeItems);
        	this.setRegistryName(new ResourceLocation(RFReference.MODID, "bones" ));	
        	this.setUnlocalizedName(this.getRegistryName().toString());
        	this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_"+ this.bones[stack.getMetadata()];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.upgradeItems)
		for (int i = 0; i < 6; i ++) {
			items.add(new ItemStack(this, 1, i));
	    }
	}

	@Override
	public Map<IRFAttributes, Integer> upgradeEffects(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
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
	public int getUpgradeDifficulty(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int itemLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void addItemLevel() {
		// TODO Auto-generated method stub
		
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 12;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+":"+meta, "inventory"));		
	}

}
