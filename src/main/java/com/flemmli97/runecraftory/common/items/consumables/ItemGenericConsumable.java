package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

public class ItemGenericConsumable extends ItemFood{

	public ItemGenericConsumable(String name) {
		super(0,0,false);
		this.setAlwaysEdible();
		this.setCreativeTab(RuneCraftory.food);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
	}

}
