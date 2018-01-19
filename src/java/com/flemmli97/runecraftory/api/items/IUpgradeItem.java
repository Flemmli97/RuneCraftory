package com.flemmli97.runecraftory.api.items;

import java.util.Map;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;

import net.minecraft.item.ItemStack;

public interface IUpgradeItem {

	public Map<IRFAttributes, Integer> upgradeEffects(ItemStack stack);

}
