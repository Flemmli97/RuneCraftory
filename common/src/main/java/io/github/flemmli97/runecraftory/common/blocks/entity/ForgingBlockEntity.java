package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ForgingBlockEntity extends UpgradingCraftingBlockEntity {

    public ForgingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.FORGING_TILE.get(), CraftingType.FORGE, blockPos, blockState);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0) {
            Equipable equipable = Equipable.get(stack);
            return (equipable == null || equipable.getEquipmentSlot() == EquipmentSlot.MAINHAND) && ItemComponentUtils.shouldHaveStats(stack);
        }
        return true;
    }
}
