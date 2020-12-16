package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockCrop extends BushBlock implements IGrowable {

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public static final IntegerProperty STATUS = IntegerProperty.create("status", 0, 3);

    private final Supplier<Item> crop;
    private final Supplier<Item> giant;
    private final Supplier<Item> seed;

    public BlockCrop(AbstractBlock.Properties prop, Supplier<Item> crop, Supplier<Item> giant, Supplier<Item> seed) {
        super(prop);
        this.setDefaultState(this.getDefaultState().with(STATUS, 0));
        this.crop = crop;
        this.giant = giant;
        this.seed = seed;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STATUS);
    }

    @Override
    public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult res) {
        TileCrop tile = (TileCrop) world.getTileEntity(pos);
        if (tile.age() >= this.properties().growth() && player.getHeldItem(hand).isEmpty()) {
            spawnDrops(state, world, pos, tile);
            if (this.properties().regrowable()) {
                tile.resetAge();
                world.setBlockState(pos, state.with(STATUS, 0));
            } else
                world.removeBlock(pos, false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.get(STATUS)];
    }

    public CropProperties properties() {
        CropProperties prop = DataPackHandler.getCropStat(this.crop.get());
        if (prop == null)
            prop = CropProperties.defaultProp;
        return prop;
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return state.getBlock() instanceof FarmlandBlock;
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return (world.getBaseLightLevel(pos, 0) >= 8 || world.isSkyVisible(pos)) && super.isValidPosition(state, world, pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = super.getDrops(state, builder);
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileCrop && ((TileCrop) tile).age() >= this.properties().growth())
            list.forEach(this::modifyStack);
        return list;
    }

    private void modifyStack(ItemStack stack) {
        if (stack.getItem() == this.crop.get() || stack.getItem() == this.giant.get()) {
            CropProperties prop = DataPackHandler.getCropStat(this.crop.get());
            if (prop != null)
                stack.setCount(prop.maxDrops());
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
        return !((TileCrop) world.getTileEntity(pos)).isFullyGrown(this);
    }

    /**
     * Use as a soil fertilizer?
     */
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Used for bonemeal items.
     */
    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {

    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
        return ItemNBT.getLeveledItem(new ItemStack(this.seed.get(), 1), 1);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCrop();
    }
}