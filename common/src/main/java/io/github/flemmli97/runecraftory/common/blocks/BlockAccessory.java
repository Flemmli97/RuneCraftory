package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.AccessoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockAccessory extends BlockCrafting {

    public static final VoxelShape[] SHAPES_LEFT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 13, 0, 16, 15, 16),
            ShapeBuilder.of(13, 0, 0, 16, 13, 3),
            ShapeBuilder.of(7, 7, 0.5, 13, 13, 2.5),
            ShapeBuilder.of(7, 7, 13.5, 13, 13, 15.5),
            ShapeBuilder.of(13, 0, 13, 16, 13, 16),
            ShapeBuilder.of(11, 14.25, 3, 13, 15.25, 5),
            ShapeBuilder.of(12, 14.25, 6, 14, 16.25, 8));
    public static final VoxelShape[] SHAPES_RIGHT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 13, 0, 16, 15, 16),
            ShapeBuilder.of(0, 0, 0, 3, 13, 3),
            ShapeBuilder.of(3, 6, 0.5, 10, 13, 2.5),
            ShapeBuilder.of(3, 6, 13.5, 10, 13, 15.5),
            ShapeBuilder.of(0, 0, 13, 3, 13, 16),
            ShapeBuilder.of(5, 14.25, 3, 14, 15.25, 12),
            ShapeBuilder.of(5, 14.25, 14, 16, 15.25, 15),
            ShapeBuilder.of(1, 15, 7, 4, 16, 14));

    public BlockAccessory(Properties props) {
        super(EnumCrafting.ARMOR, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockCrafting.PART) == EnumPart.LEFT)
            return SHAPES_LEFT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
        return SHAPES_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new AccessoryBlockEntity(pos, state);
    }
}
