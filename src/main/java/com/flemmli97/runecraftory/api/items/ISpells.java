package com.flemmli97.runecraftory.api.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpells extends IItemBase{
	
	public void levelSkill(EntityPlayer player);
	
	public int coolDown();
	
	public boolean use(World world, EntityPlayer player, ItemStack stack);
	
	public void update(ItemStack stack, EntityPlayer player);
}
