package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGreenifier extends ItemFertilizer{

	private boolean strong;

	public ItemGreenifier(boolean strongVersion) {
		super(strongVersion?"greenifier_plus":"greenifier");
		this.strong = strongVersion;
	}

	@Override
	protected boolean useItemOnFarmland(ItemStack stack, World world, TileFarmland tile, EntityPlayer player,
			EnumHand hand) {
		tile.applyLevelFertilizer(this.strong?0.5F:0.2F);
		return true;
	}

}
