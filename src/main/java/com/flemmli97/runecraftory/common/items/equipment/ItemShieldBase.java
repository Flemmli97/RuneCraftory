package com.flemmli97.runecraftory.common.items.equipment;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemShieldBase extends Item implements IItemWearable
{    
    public ItemShieldBase(String name) {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.equipment);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
