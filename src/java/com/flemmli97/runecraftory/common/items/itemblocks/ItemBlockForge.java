package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.blocks.BlockMultiBase;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockForge extends ItemBlockBase {
	
	public ItemBlockForge()
	{
		super((BlockMultiBase) ModBlocks.forge);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, "forgeItem"));	
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
