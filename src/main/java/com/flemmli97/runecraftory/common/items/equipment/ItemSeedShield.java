package com.flemmli97.runecraftory.common.items.equipment;

import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;

public class ItemSeedShield extends ItemShieldBase{

	public ItemSeedShield() {
		super("seed_shield_item");
        this.setCreativeTab(null);
		CropMap.addCrop(LibOreDictionary.SEEDSHIELDITEM, this);
	}

}
