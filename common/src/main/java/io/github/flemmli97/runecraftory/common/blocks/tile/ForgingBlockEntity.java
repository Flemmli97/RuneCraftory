package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ForgingBlockEntity extends CraftingBlockEntity {

    public ForgingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.forgingTile.get(), EnumCrafting.FORGE, blockPos, blockState);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 6) {
            EquipmentSlot slotType = Platform.INSTANCE.slotType(stack);
            return (slotType == null || slotType == EquipmentSlot.MAINHAND) && ItemNBT.shouldHaveStats(stack);
        }
        return true;
    }
}
