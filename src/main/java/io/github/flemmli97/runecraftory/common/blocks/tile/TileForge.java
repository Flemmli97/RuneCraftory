package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class TileForge extends TileCrafting {

    public TileForge() {
        super(ModBlocks.forgingTile.get(), EnumCrafting.FORGE);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 6) {
            EquipmentSlotType slotType = stack.getItem().getEquipmentSlot(stack);
            return (slotType == null || slotType == EquipmentSlotType.MAINHAND) && ItemNBT.shouldHaveStats(stack);
        }
        return true;
    }
}
