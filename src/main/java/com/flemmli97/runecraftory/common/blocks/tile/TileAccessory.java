package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.item.ItemStack;

public class TileAccessory extends TileMultiBase{

	public TileAccessory() {
		super(EnumCrafting.ARMOR);
	}

	@Override
	public String getName() {
		return "tile.accessory";
	}

	@Override
	protected boolean validItem(ItemStack stack) {
		return stack.getItem() instanceof IItemWearable && !(stack.getItem() instanceof IItemUsable);

	}
}
