package io.github.flemmli97.runecraftory.common.entities.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

/**
 * Makes them not get stuck on trapdoors and go over rails
 */
public class NPCWalkNodeEvaluator extends WalkNodeEvaluator {

    @Nullable
    @Override
    protected Node findAcceptedNode(int i, int j, int k, int l, double d, Direction direction, BlockPathTypes blockPathTypes) {
        Node node = super.findAcceptedNode(i, j, k, l, d, direction, blockPathTypes);
        if (node != null && node.type == BlockPathTypes.TRAPDOOR) {
            BlockState state = this.level.getBlockState(new BlockPos(node.x, node.y, node.z));
            if (!(state.getBlock() instanceof TrapDoorBlock))
                return node;
            boolean tryGoOver = false;
            if (state.getValue(TrapDoorBlock.OPEN)) {
                Direction facing = state.getValue(TrapDoorBlock.FACING);
                if (facing == direction.getOpposite() || facing == direction) {
                    tryGoOver = true;
                }
            } else if (state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                tryGoOver = true;
            }
            if (tryGoOver) {
                return this.findAcceptedNode(i, j + 1, k, l, d, direction, blockPathTypes);
            }
        }
        return node;
    }

    @Override
    protected BlockPathTypes evaluateBlockPathType(BlockGetter level, boolean canOpenDoors, boolean canEnterDoors, BlockPos pos, BlockPathTypes nodeType) {
        BlockPathTypes t = super.evaluateBlockPathType(level, canOpenDoors, canEnterDoors, pos, nodeType);
        if (t == BlockPathTypes.UNPASSABLE_RAIL)
            return BlockPathTypes.OPEN;
        return t;
    }
}
