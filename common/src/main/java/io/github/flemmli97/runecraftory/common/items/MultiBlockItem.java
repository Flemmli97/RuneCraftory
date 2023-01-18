package io.github.flemmli97.runecraftory.common.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface MultiBlockItem {

    List<Pair<BlockPos, BlockState>> getBlocks();
}
