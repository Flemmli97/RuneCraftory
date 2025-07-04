package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.api.enums.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AccessoryBlockEntity extends UpgradingCraftingBlockEntity {

    public AccessoryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.ACCESSORY_TILE.get(), CraftingType.ACCESSORY_WORKBENCH, blockPos, blockState);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0) {
            Equipable slotType = Equipable.get(stack);
            return slotType != null && slotType.getEquipmentSlot() != EquipmentSlot.MAINHAND && ItemNBT.shouldHaveStats(stack);
        }
        return true;
    }
}
