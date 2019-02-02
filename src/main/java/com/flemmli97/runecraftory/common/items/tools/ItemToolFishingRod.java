package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemToolFishingRod extends ItemFishingRod implements IItemUsable, IChargeable{

	private EnumToolTier tier;

	public ItemToolFishingRod(EnumToolTier tier) {
		super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "fishing_rod_" + tier.getName()));
        this.setUnlocalizedName(this.getRegistryName().toString());	
        this.tier = tier;
	}

	@Override
	public int[] getChargeTime() {
		int charge = 15;
        if (this.tier == EnumToolTier.PLATINUM) {
            charge = 7;
        }
        return new int[] { charge, this.tier.getTierLevel()};
	}

	@Override
	public EnumToolCharge chargeType(ItemStack stack) {
		return EnumToolCharge.CHARGEUPWEAPON;
	}

	@Override
	public EnumWeaponType getWeaponType() {
		return EnumWeaponType.HAXE;
	}

	@Override
	public int itemCoolDownTicks() {
		return 19;
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
