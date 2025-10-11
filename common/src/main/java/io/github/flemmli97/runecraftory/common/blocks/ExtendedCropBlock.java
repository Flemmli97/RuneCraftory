package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.blocks.util.Growable;
import io.github.flemmli97.runecraftory.common.blocks.util.GrowableCrop;
import io.github.flemmli97.runecraftory.common.blocks.util.LazyResolvedRegistryEntry;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExtendedCropBlock extends CropBlock implements GrowableCrop, Growable {

    public static final MapCodec<ExtendedCropBlock> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(propertiesCodec(),
                    LazyResolvedRegistryEntry.codec(Registries.ITEM).fieldOf("crop").forGetter(d -> d.crop),
                    LazyResolvedRegistryEntry.codec(Registries.ITEM).fieldOf("seed").forGetter(d -> d.seed)
            ).apply(inst, ExtendedCropBlock::new));

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);
    public static final BooleanProperty WILTED = BooleanProperty.create("wilted");
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)};
    protected final LazyResolvedRegistryEntry<Item> crop;
    protected final LazyResolvedRegistryEntry<Item> seed;

    public ExtendedCropBlock(BlockBehaviour.Properties prop, ResourceKey<Item> crop, ResourceKey<Item> seed) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState().setValue(this.getAgeProperty(), 0).setValue(WILTED, false));
        this.crop = new LazyResolvedRegistryEntry<>(crop);
        this.seed = new LazyResolvedRegistryEntry<>(seed);
    }

    private ExtendedCropBlock(BlockBehaviour.Properties prop, LazyResolvedRegistryEntry<Item> crop, LazyResolvedRegistryEntry<Item> seed) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState().setValue(this.getAgeProperty(), 0).setValue(WILTED, false));
        this.crop = crop;
        this.seed = seed;
    }

    @Override
    public MapCodec<ExtendedCropBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return Items.AIR;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.seed.get(level.registryAccess()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(WILTED);
    }

    public Item getCrop(HolderLookup.Provider provider) {
        return this.crop.get(provider).value();
    }

    public Item getCrop(HolderLookup<Item> provider) {
        return this.crop.get(provider).value();
    }

    @Override
    public void onWither(int amount, Level level, BlockState state, BlockPos pos) {
        if (amount > 1 || state.getValue(ExtendedCropBlock.WILTED)) {
            level.setBlock(pos, RuneCraftoryBlocks.WITHERED_GRASS.get().defaultBlockState(), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(ExtendedCropBlock.WILTED, true), Block.UPDATE_ALL);
        }
    }

    @Override
    public void onWater(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(ExtendedCropBlock.WILTED))
            level.setBlock(pos, state.setValue(ExtendedCropBlock.WILTED, false), Block.UPDATE_ALL);
    }

    public int getGiantAge() {
        return 4;
    }

    @Override
    public int runecraftory$getGrowableMaxAge() {
        return this.getMaxAge();
    }

    @Override
    public BlockState runecraftory$getGrowableStateForAge(BlockState current, int age) {
        return this.getStateForAge(age);
    }

    @Override
    public boolean runecraftory$isAtMaxAge(BlockState state) {
        return this.isMaxAge(state);
    }
}