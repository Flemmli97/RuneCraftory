package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.blocks.tile.CropBlockEntity;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class BlockCrop extends BushBlock implements BonemealableBlock, EntityBlock {

    private static final AABB[] CROPS_AABB = new AABB[]{new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private final Supplier<Item> crop;
    private final Supplier<Item> giant;
    private final Supplier<Item> seed;

    public BlockCrop(BlockBehaviour.Properties prop, Supplier<Item> crop, Supplier<Item> giant, Supplier<Item> seed) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
        this.crop = crop;
        this.giant = giant;
        this.seed = seed;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (this.isMaxAge(state, level, pos)) {
            BlockEntity tile = level.getBlockEntity(pos);
            dropResources(state, level, pos, tile);
            if (tile instanceof CropBlockEntity && this.properties().map(CropProperties::regrowable).orElse(false)) {
                ((CropBlockEntity) tile).resetAge();
                level.setBlockAndUpdate(pos, state.setValue(AGE, 0));
            } else
                level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean isMaxAge(BlockState state, Level level, BlockPos pos) {
        return state.getValue(AGE) == 3;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    public Optional<CropProperties> properties() {
        return Optional.ofNullable(DataPackHandler.getCropStat(this.seed.get()));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModTags.farmland);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && super.canSurvive(state, level, pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof CropBlockEntity crop && this.properties().map(p -> crop.age() >= p.growth()).orElse(false))
            list.forEach(stack -> this.modifyStack(stack, crop));
        else
            list.clear();
        return list;
    }

    private void modifyStack(ItemStack stack, CropBlockEntity tile) {
        CropProperties prop = DataPackHandler.getCropStat(this.crop.get());
        if (prop != null)
            stack.setCount(prop.maxDrops());
        ItemNBT.getLeveledItem(stack, tile.level());
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return !((CropBlockEntity) level.getBlockEntity(pos)).isFullyGrown(this);
    }

    /**
     * Use as a soil fertilizer?
     */
    @Override
    public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Used for bonemeal items.
     */
    @Override
    public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state) {

    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemNBT.getLeveledItem(super.getCloneItemStack(level, pos, state), 1);
    }

    public Item getCrop() {
        return this.crop.get();
    }

    public Item getGiantCrop() {
        return this.giant.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CropBlockEntity(pos, state);
    }
}