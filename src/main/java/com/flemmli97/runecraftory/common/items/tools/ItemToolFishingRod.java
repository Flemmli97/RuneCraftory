package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class ItemToolFishingRod extends FishingRodItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolFishingRod(EnumToolTier tier, Properties props) {
        super(props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if(this.tier == EnumToolTier.PLATINUM)
            return (int) (GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        if(this.tier == EnumToolTier.PLATINUM)
            return this.tier.getTierLevel();
        return this.tier.getTierLevel()+1;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEFISHING;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayerEntity player) {

    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {

    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}
