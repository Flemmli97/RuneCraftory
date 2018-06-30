package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.item.ItemStack;

public class TileForge extends TileMultiBase{

	public TileForge() {
		super(EnumCrafting.FORGE);
	}

	@Override
	public String getName() {
		return "tile.forge";
	}

	@Override
	protected boolean validItem(ItemStack stack) {
		return stack.getItem() instanceof IItemUsable;
	}
}
