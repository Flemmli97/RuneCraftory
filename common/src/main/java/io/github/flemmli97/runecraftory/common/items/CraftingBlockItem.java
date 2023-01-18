package io.github.flemmli97.runecraftory.common.items;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class CraftingBlockItem extends BlockItem implements MultiBlockItem {

    private final Supplier<List<Pair<BlockPos, BlockState>>> states;

    public CraftingBlockItem(Block block, Properties properties) {
        super(block, properties);
        this.states = Suppliers.memoize(() -> List.of(Pair.of(BlockPos.ZERO, block.defaultBlockState()),
                Pair.of(new BlockPos(1, 0, 0), block.defaultBlockState().setValue(BlockCrafting.PART, BlockCrafting.EnumPart.RIGHT))));
    }

    @Override
    public List<Pair<BlockPos, BlockState>> getBlocks() {
        return this.states.get();
    }
}