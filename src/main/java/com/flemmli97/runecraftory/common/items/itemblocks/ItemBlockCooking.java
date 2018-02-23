package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.blocks.BlockCookingBench;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockCooking extends ItemBlockBase {
	
	public ItemBlockCooking()
	{
		super((BlockCookingBench) ModBlocks.cooking);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "cookingItem"));	
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
}
