package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolPetInspector extends Item implements IItemBase{

	private int level = 1;
	public ItemToolPetInspector() {
		super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, "inspector"));	
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getRegistryName().toString();
	}
	
	@Override
	public int getBuyPrice(ItemStack stack) {
		return 2500;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		return 700;
	}

	@Override
	public int getUpgradeDifficulty(ItemStack stack) {
		return 0;
	}

	@Override
	public int itemLevel() {
		return level;
	}

	@Override
	public void addItemLevel() {
		this.level+=1;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}

}
