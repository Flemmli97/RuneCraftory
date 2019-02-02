package com.flemmli97.runecraftory.common.items.special.spells;

import com.flemmli97.runecraftory.common.items.special.ItemCast;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Empty spell so the items can get registered first
 */
public class ItemEmptySkill extends ItemCast{

	public ItemEmptySkill(String name) {
		super(name);
	}

	@Override
	public void levelSkill(EntityPlayer player) {
	}

	@Override
	public int coolDown() {
		return 0;
	}

	@Override
	public boolean use(World world, EntityPlayer player, ItemStack stack) {
		return true;
	}

}
