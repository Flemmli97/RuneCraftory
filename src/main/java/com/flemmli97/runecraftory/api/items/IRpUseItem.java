package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

public interface IRpUseItem extends IItemWearable{
	
	public int[] getChargeTime();
	
	public EnumWeaponType getWeaponType();
	
	public int itemCoolDownTicks();

}
