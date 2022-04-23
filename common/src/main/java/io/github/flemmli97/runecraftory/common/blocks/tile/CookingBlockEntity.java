package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CookingBlockEntity extends CraftingBlockEntity {

    public CookingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.cookingTile.get(), EnumCrafting.COOKING, blockPos, blockState);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}
