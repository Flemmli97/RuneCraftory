package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;

public interface IItemUsable extends IItemWearable
{
	public EnumWeaponType getWeaponType();
    
	public int itemCoolDownTicks();
    
	public void levelSkillOnHit(EntityPlayer player);
    
	public void levelSkillOnBreak(EntityPlayer player);
}
