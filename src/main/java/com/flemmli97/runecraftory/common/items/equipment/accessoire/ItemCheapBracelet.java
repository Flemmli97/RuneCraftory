package com.flemmli97.runecraftory.common.items.equipment.accessoire;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.items.equipment.ItemAccessoireBase;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCheapBracelet extends ItemAccessoireBase
{
    private static ItemArmor.ArmorMaterial mat = EnumHelper.addArmorMaterial("runeCraftory_armorMat", "", 0, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0f);
    
    public ItemCheapBracelet() {
        super(ItemCheapBracelet.mat, "cheap_bracelet");
    }
    
    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ModelBiped getArmorModel(final EntityLivingBase entityLiving, final ItemStack itemStack, final EntityEquipmentSlot armorSlot, final ModelBiped _default) {
        _default.setVisible(false);
        return _default;
    }
}
