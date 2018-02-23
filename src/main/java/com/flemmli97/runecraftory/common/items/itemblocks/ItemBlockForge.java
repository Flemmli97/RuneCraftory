package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.blocks.BlockForge;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockForge extends ItemBlockBase {
	
	public ItemBlockForge()
	{
		super((BlockForge) ModBlocks.forge);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "forgeItem"));	
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
