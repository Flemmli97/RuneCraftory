package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWettablePowder extends ItemFertilizer{

	public ItemWettablePowder() {
		super("wettable_powder");
	}

	@Override
	protected boolean useItemOnFarmland(ItemStack stack, World world, TileFarmland tile, EntityPlayer player,
			EnumHand hand) {
		tile.applyHealth(50);
		return true;
	}

}
