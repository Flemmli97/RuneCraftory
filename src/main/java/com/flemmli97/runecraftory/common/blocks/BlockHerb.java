package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockHerb extends BushBlock {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public static final IntegerProperty LEVEL = IntegerProperty.create("variant", 0, 10);
    private final Supplier<Item> item;

    public BlockHerb(AbstractBlock.Properties props, Supplier<Item> item) {
        super(props);
        this.item = item;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LEVEL);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = super.getDrops(state, builder);
        int level = state.get(LEVEL);
        list.forEach(stack-> this.modify(stack, level, builder.getWorld().rand));
        return list;
    }

    private void modify(ItemStack stack, int level, Random random){
        if(stack.getItem() == this.item.get()) {
            if (level == 0)
                level = MathHelper.clamp(random.nextInt(5) + random.nextInt(4) + random.nextInt(3) + random.nextInt(2), 1, 10);
            ItemNBT.getLeveledItem(stack, level);
        }
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        return ItemNBT.getLeveledItem(new ItemStack(this.item.get(), 1), 1);
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
}