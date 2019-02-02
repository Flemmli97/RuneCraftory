package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemIcon extends Item{
	public ItemIcon(int number)
    {
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "icon_"+number));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setHasSubtypes(true);
    }
}
