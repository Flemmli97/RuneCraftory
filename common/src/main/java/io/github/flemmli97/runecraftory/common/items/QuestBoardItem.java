package io.github.flemmli97.runecraftory.common.items;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.blocks.QuestboardBlock;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class QuestBoardItem extends BlockItem implements MultiBlockItem {

    private final Supplier<List<Pair<BlockPos, BlockState>>> states = Suppliers.memoize(() -> QuestboardBlock.getPosMap(BlockPos.ZERO, RuneCraftoryBlocks.QUEST_BOARD.get().defaultBlockState())
            .stream().map(p -> Pair.of(p.getSecond(), RuneCraftoryBlocks.QUEST_BOARD.get().defaultBlockState().setValue(QuestboardBlock.PART, p.getFirst())))
            .toList());

    public QuestBoardItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public List<Pair<BlockPos, BlockState>> getBlocks() {
        return this.states.get();
    }
}
