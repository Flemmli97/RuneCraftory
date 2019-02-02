package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemInstaTame extends Item{
	public ItemInstaTame()
    {
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "insta_tame"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
