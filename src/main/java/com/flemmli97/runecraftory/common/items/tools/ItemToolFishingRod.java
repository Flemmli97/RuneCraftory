package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.EnumToolCharge;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
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
    public int[] getChargeTime() {
        return new int[0];
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
        return 15;
    }

    @Override
    public void onEntityHit(PlayerEntity player) {

    }

    @Override
    public void onBlockBreak(PlayerEntity player) {

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
