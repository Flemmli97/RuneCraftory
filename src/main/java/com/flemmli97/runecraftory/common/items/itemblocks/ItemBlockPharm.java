package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.blocks.BlockPharmacy;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockPharm extends ItemBlockBase {
	
	public ItemBlockPharm()
	{
		super((BlockPharmacy) ModBlocks.pharm);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "chemItem"));	
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
