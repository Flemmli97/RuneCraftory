package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.blocks.entity.ForgingBlockEntity;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class BlockForge extends BlockCrafting {

    public static final MapCodec<BlockForge> CODEC = simpleCodec(BlockForge::new);

    public static final VoxelShape LEFT = Stream.of(
            Block.box(0, 0, 0, 16, 12, 16),
            Block.box(2, 12, 2, 14, 14, 14),
            Block.box(4.5, 14, 4.5, 11.5, 16, 11.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape[] SHAPES_RIGHT = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0, 0, 4, 16, 5, 13),
            VoxelUtils.ShapeBuilder.of(0, 5, 4, 16, 6, 13));

    public BlockForge(Properties props) {
        super(props);
    }

    @Override
    public MapCodec<BlockForge> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockCrafting.PART) == EnumPart.LEFT)
            return LEFT;
        return SHAPES_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new ForgingBlockEntity(pos, state);
    }
}
