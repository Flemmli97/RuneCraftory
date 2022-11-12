package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AccessoryBlockEntity extends UpgradingCraftingBlockEntity {

    public AccessoryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.accessoryTile.get(), EnumCrafting.ARMOR, blockPos, blockState);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0) {
            EquipmentSlot slotType = LivingEntity.getEquipmentSlotForItem(stack);
            return slotType != null && slotType != EquipmentSlot.MAINHAND && ItemNBT.shouldHaveStats(stack);
        }
        return true;
    }
}
