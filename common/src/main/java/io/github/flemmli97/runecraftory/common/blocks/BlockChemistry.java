package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.ChemistryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockChemistry extends BlockCrafting {

    public static final VoxelShape[] SHAPES_LEFT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 0, 1, 16, 12, 16),
            ShapeBuilder.of(0, 12, 0, 16, 14, 16),
            ShapeBuilder.of(7, 15, 3, 10, 16, 6),
            ShapeBuilder.of(9, 14, 3, 10, 15, 4),
            ShapeBuilder.of(7, 14, 3, 8, 15, 4),
            ShapeBuilder.of(9, 14, 5, 10, 15, 6),
            ShapeBuilder.of(7, 14, 5, 8, 15, 6),
            ShapeBuilder.of(9, 16, 4, 9, 17, 5),
            ShapeBuilder.of(1, 13.1, 4, 2, 14.1, 9));
    public static final VoxelShape[] SHAPES_RIGHT = BlockCrafting.joinedOrDirs(ShapeBuilder.of(0, 0, 1, 16, 12, 16),
            ShapeBuilder.of(0, 12, 0, 16, 14, 16),
            ShapeBuilder.of(10, 13.25, 4, 14, 14.25, 8),
            ShapeBuilder.of(1, 14, 13, 3, 16, 15),
            ShapeBuilder.of(1.5, 16, 13.5, 2.5, 17, 14.5),
            ShapeBuilder.of(4, 14, 13, 6, 16, 15),
            ShapeBuilder.of(4.5, 16, 13.5, 5.5, 17, 14.5),
            ShapeBuilder.of(7, 14, 13, 9, 16, 15),
            ShapeBuilder.of(7.5, 16, 13.5, 8.5, 17, 14.5),
            ShapeBuilder.of(10, 14, 13, 12, 16, 15),
            ShapeBuilder.of(10.5, 16, 13.5, 11.5, 17, 14.5),
            ShapeBuilder.of(13, 14, 13, 15, 16, 15),
            ShapeBuilder.of(13.5, 16, 13.5, 14.5, 17, 14.5),
            ShapeBuilder.of(3, 14, 3, 5, 18, 5),
            ShapeBuilder.of(5, 14, 7, 7, 18, 9));

    public BlockChemistry(Properties props) {
        super(EnumCrafting.CHEM, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockCrafting.PART) == EnumPart.LEFT)
            return SHAPES_LEFT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
        return SHAPES_RIGHT[state.getValue(BlockCrafting.FACING).get2DDataValue()];
    }

    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return new ChemistryBlockEntity(pos, state);
    }
}
