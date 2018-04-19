package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.blocks.BlockAccessoryCrafter;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockAccess extends ItemBlockBase {
	
	public ItemBlockAccess()
	{
		super((BlockAccessoryCrafter)ModBlocks.accessory);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "block_access"));	
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
