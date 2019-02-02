package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFormular extends ItemFertilizer{

	private int type;

	public ItemFormular(int type) {
		super(type==0?"formular_a":type==1?"formular_b":"formular_c");
		this.type=type;
	}

	@Override
	protected boolean useItemOnFarmland(ItemStack stack, World world, TileFarmland tile, EntityPlayer player,
			EnumHand hand) {
		tile.applyGrowthFertilizer(this.type==0?0.4F:this.type==1?1:2);
		return true;
	}

}
