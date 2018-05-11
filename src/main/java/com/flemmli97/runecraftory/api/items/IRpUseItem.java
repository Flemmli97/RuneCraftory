package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IRpUseItem extends IItemWearable{
	
	public int[] getChargeTime();
	
	public EnumWeaponType getWeaponType();
	
	public int itemCoolDownTicks();
	
	public void levelSkillOnHit(EntityPlayer player);
	
	public void levelSkillOnBreak(EntityPlayer player);

	public EnumToolCharge chargeType(ItemStack stack);
}
