package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.CookingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCooking extends BlockCrafting {

    public static final VoxelShape[] SHAPES_LEFT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 0, 1, 16, 13, 16),
            ShapeBuilder.of(0, 13, 0, 16, 15, 16));
    public static final VoxelShape[] SHAPES_RIGHT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 0, 1, 16, 13, 16),
            ShapeBuilder.of(0, 13, 0, 16, 15, 16),
            ShapeBuilder.of(6, 14.25, 1, 14, 15.25, 9),
            ShapeBuilder.of(8, 14.25, 13, 13, 15.25, 14),
            ShapeBuilder.of(9, 14.25, 11, 14, 15.25, 12),
            ShapeBuilder.of(1, 14.25, 2, 2, 15.25, 8),
            ShapeBuilder.of(2, 14.25, 4, 4, 15.25, 8),
            ShapeBuilder.of(5, 14.25, 11, 6, 16.25, 12),
            ShapeBuilder.of(5, 14.25, 13, 6, 16.25, 14));

    public BlockCooking(Properties props) {
        super(EnumCrafting.COOKING, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockCrafting.PART) == EnumPart.LEFT)
            return SHAPES_LEFT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
        return SHAPES_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new CookingBlockEntity(pos, state);
    }
}
