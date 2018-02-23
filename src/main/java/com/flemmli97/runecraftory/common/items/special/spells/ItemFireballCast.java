package com.flemmli97.runecraftory.common.items.special.spells;

import com.flemmli97.runecraftory.common.items.special.ItemCast;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFireballCast extends ItemCast{

	public ItemFireballCast() {
		super("fireball");
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void useRunePoints(EntityPlayer player, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean cast(World world, EntityPlayer player, EnumHand hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int coolDown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

}
