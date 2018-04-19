package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISpells extends IItemBase{

	public void useRunePoints(EntityPlayer player, int amount);
	
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill);
	
	public int coolDown();
	
	public boolean use(World world, EntityPlayer player);
}
