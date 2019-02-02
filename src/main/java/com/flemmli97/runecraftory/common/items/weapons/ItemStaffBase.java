package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemStaffBase extends Item implements IItemUsable, IChargeable
{
    private int chargeXP = 25;
    
    public ItemStaffBase(String name, EnumElement startElement) {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

	@Override
	public int[] getChargeTime() {
		return new int[] { 25, 1 };
	}

	@Override
	public EnumToolCharge chargeType(ItemStack stack) {
		return EnumToolCharge.CHARGEUPTOOL;
	}

	@Override
	public EnumWeaponType getWeaponType() {
		return EnumWeaponType.STAFF;
	}

	@Override
	public int itemCoolDownTicks() {
		return 15;
	}

	@Override
	public void levelSkillOnHit(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void levelSkillOnBreak(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
}

