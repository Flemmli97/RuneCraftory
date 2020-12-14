package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemToolAxe extends AxeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolAxe(EnumToolTier tier, Item.Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int[] getChargeTime() {
        int charge = 15;
        if (this.tier == EnumToolTier.PLATINUM) {
            charge = 7;
        }
        return new int[]{charge, this.tier.getTierLevel()};
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
    public void onEntityHit(PlayerEntity player) {

    }

    @Override
    public void onBlockBreak(PlayerEntity player) {

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
