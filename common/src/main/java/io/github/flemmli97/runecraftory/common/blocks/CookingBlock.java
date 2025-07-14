package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.blocks.entity.CookingBlockEntity;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CookingBlock extends CraftingBlock {

    public static final MapCodec<CookingBlock> CODEC = simpleCodec(CookingBlock::new);

    public static final VoxelShape[] SHAPES_LEFT = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0, 0, 1, 16, 13, 16),
            VoxelUtils.ShapeBuilder.of(0, 13, 0, 16, 15, 16));
    public static final VoxelShape[] SHAPES_RIGHT = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0, 0, 1, 16, 13, 16),
            VoxelUtils.ShapeBuilder.of(0, 13, 0, 16, 15, 16),
            VoxelUtils.ShapeBuilder.of(6, 14.25, 1, 14, 15.25, 9),
            VoxelUtils.ShapeBuilder.of(8, 14.25, 13, 13, 15.25, 14),
            VoxelUtils.ShapeBuilder.of(9, 14.25, 11, 14, 15.25, 12),
            VoxelUtils.ShapeBuilder.of(1, 14.25, 2, 2, 15.25, 8),
            VoxelUtils.ShapeBuilder.of(2, 14.25, 4, 4, 15.25, 8),
            VoxelUtils.ShapeBuilder.of(5, 14.25, 11, 6, 16.25, 12),
            VoxelUtils.ShapeBuilder.of(5, 14.25, 13, 6, 16.25, 14));

    public CookingBlock(Properties props) {
        super(props);
    }

    @Override
    public MapCodec<CookingBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(CraftingBlock.PART) == EnumPart.LEFT)
            return SHAPES_LEFT[state.getValue(CraftingBlock.FACING).get2DDataValue()];
        return SHAPES_RIGHT[state.getValue(CraftingBlock.FACING).get2DDataValue()];
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new CookingBlockEntity(pos, state);
    }
}
