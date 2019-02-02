package com.flemmli97.runecraftory.common.items.weapons.shortsword;

import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;

public class ItemSeedSword extends ItemShortSwordBase{

	public ItemSeedSword() {
		super("seed_sword_item");
        this.setCreativeTab(null);
		CropMap.addCrop(LibOreDictionary.SEEDSWORDITEM, this);
	}

}
