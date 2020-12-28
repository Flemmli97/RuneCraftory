package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.loot.IBlockModifyLevel;
import com.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;
import java.util.Set;

public class BlockHerb extends BushBlock implements IBlockModifyLevel {

    public static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public static final IntegerProperty LEVEL = IntegerProperty.create("variant", 0, 10);

    private final Set<GroundTypes> types = EnumSet.noneOf(GroundTypes.class);

    public BlockHerb(AbstractBlock.Properties props, GroundTypes... types) {
        super(props);
        for (GroundTypes type : types)
            this.types.add(type);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public int getLevel(BlockState state, TileEntity tile, ItemLevelLootFunction func, LootContext ctx) {
        int level = state.get(LEVEL);
        return level == 0 ? func.getLevel(ctx) : level;
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
        return ItemNBT.getLeveledItem(super.getItem(world, pos, state), 1);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader reader, BlockPos pos) {
        if (this.types.contains(GroundTypes.NETHER))
            if (state.isIn(Tags.Blocks.NETHERRACK) || state.isIn(BlockTags.NYLIUM))
                return true;
        if (this.types.contains(GroundTypes.END))
            if (state.isIn(Tags.Blocks.END_STONES))
                return true;
        if (this.types.contains(GroundTypes.SANDY))
            if (state.isIn(Tags.Blocks.SAND))
                return true;
        return state.isIn(Tags.Blocks.DIRT) || super.isValidGround(state, reader, pos);
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType() {
        return AbstractBlock.OffsetType.XZ;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        Vector3d vector3d = p_220053_1_.getOffset(p_220053_2_, p_220053_3_);
        return SHAPE.withOffset(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(LEVEL, ItemNBT.itemLevel(context.getItem()));
    }

    public enum GroundTypes {
        NETHER,
        END,
        SANDY
    }
}