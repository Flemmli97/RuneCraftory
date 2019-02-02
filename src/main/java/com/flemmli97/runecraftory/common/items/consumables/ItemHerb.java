package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.CropMap;

public class ItemHerb extends ItemGenericConsumable{

	public ItemHerb(String name, String block) {
		super(name);
		this.setCreativeTab(RuneCraftory.medicine);
		CropMap.addCrop(block, this);
	}
}
