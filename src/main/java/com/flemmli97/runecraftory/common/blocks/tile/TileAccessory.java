package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class TileAccessory extends TileCrafting {

    public TileAccessory() {
        super(ModBlocks.accessoryTile.get(), EnumCrafting.ARMOR);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 6) {
            EquipmentSlotType type = stack.getItem().getEquipmentSlot(stack);
            return type != null && type != EquipmentSlotType.MAINHAND;
        }
        return true;
    }
}
