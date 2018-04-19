package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;

public interface IRpUseItem extends IItemWearable{
	
	public int[] getChargeTime();
	
	public EnumWeaponType getWeaponType();
	
	public int itemCoolDownTicks();
	
	public void levelSkillOnUse(EntityPlayer player);

}
