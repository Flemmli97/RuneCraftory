package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;

public interface IRpUseItem {
	
	public void useRunePoints(EntityPlayer player, int amount);
	
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill);
	
	public int[] getChargeTime();
	
	public EnumWeaponType getWeaponType();
	
	public int itemCoolDownTicks();

}
