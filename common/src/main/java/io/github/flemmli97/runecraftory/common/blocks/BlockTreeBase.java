package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixinhelper.LevelSnapshotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockTreeBase extends RotatedPillarBlock implements EntityBlock, Growable {

    public static final MapCodec<BlockTreeBase> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(propertiesCodec(),
                    ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("stump").forGetter(d -> d.stump),
                    ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("stage_1").forGetter(d -> d.stage1),
                    ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("stage_2").forGetter(d -> d.stage2),
                    LazyResolvedRegistryEntry.codec(Registries.ITEM).fieldOf("seed").forGetter(d -> d.sapling)
            ).apply(inst, BlockTreeBase::new));

    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private final ResourceKey<ConfiguredFeature<?, ?>> stump;
    private final ResourceKey<ConfiguredFeature<?, ?>> stage1;
    private final ResourceKey<ConfiguredFeature<?, ?>> stage2;
    protected final LazyResolvedRegistryEntry<Item> sapling;

    public BlockTreeBase(Properties properties, ResourceKey<ConfiguredFeature<?, ?>> stump, ResourceKey<ConfiguredFeature<?, ?>> stage1,
                         ResourceKey<ConfiguredFeature<?, ?>> stage2, ResourceKey<Item> seed) {
        super(properties);
        this.stump = stump;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.sapling = new LazyResolvedRegistryEntry<>(seed);
    }

    private BlockTreeBase(BlockBehaviour.Properties prop, ResourceKey<ConfiguredFeature<?, ?>> stump, ResourceKey<ConfiguredFeature<?, ?>> stage1,
                          ResourceKey<ConfiguredFeature<?, ?>> stage2, LazyResolvedRegistryEntry<Item> seed) {
        super(prop);
        this.stump = stump;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.sapling = seed;
    }

    @Override
    public MapCodec<BlockTreeBase> codec() {
        return CODEC;
    }

    public static boolean isAirOrReplaceable(BlockState state) {
        return state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(BlockTags.LEAVES);
    }

    public boolean growTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource rand) {
        return switch (state.getValue(AGE)) {
            case 2 -> {
                ((LevelSnapshotHandler) level).runecraftory$getSnapshotHandler().takeSnapshot(null);
                if (level.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                    tree.onRemove(level, false);
                }
                boolean result = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)
                        .getOrThrow(this.stage2).value().place(level, level.getChunkSource().getGenerator(), rand, pos);
                ((LevelSnapshotHandler) level).runecraftory$getSnapshotHandler().popSnapshots(result);
                yield result;
            }
            case 1 -> {
                ((LevelSnapshotHandler) level).runecraftory$getSnapshotHandler().takeSnapshot(null);
                if (level.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                    tree.onRemove(level, false);
                }
                boolean result = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)
                        .getOrThrow(this.stage1).value().place(level, level.getChunkSource().getGenerator(), rand, pos);
                ((LevelSnapshotHandler) level).runecraftory$getSnapshotHandler().popSnapshots(result);
                yield result;
            }
            case 0 -> level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE)
                    .getOrThrow(this.stump).value().place(level, level.getChunkSource().getGenerator(), rand, pos);
            default -> {
                if (level.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                    tree.update(level);
                }
                yield false;
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level instanceof ServerLevel serverLevel && !newState.is(state.getBlock())) {
            FarmlandHandler.get(serverLevel.getServer()).getData(serverLevel, pos.below())
                    .ifPresent(d -> d.onCropRemove(serverLevel, pos, newState));
            if (serverLevel.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                tree.onRemove(level, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TreeBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;//new ItemStack(this.seedItem.get());
    }

    @Override
    public int runecraftory$getGrowableMaxAge() {
        return MAX_AGE;
    }

    @Override
    public boolean canGrow(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == 5)
            return BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 2, 1))
                    .allMatch(p -> p.equals(pos) || BlockTreeBase.isAirOrReplaceable(level.getBlockState(p)));
        return Growable.super.canGrow(level, pos, state);
    }

    @Override
    public BlockState runecraftory$getGrowableStateForAge(BlockState current, int age) {
        int newAge = current.getOptionalValue(AGE).map(i -> Mth.clamp(age - 1, 0, i + 1)).orElse(age - 1);
        return this.defaultBlockState().setValue(AGE, newAge);
    }

    @Override
    public boolean runecraftory$isAtMaxAge(BlockState state) {
        return state.getValue(AGE) == 4;
    }

    @Override
    public void onGrow(ServerLevel level, BlockPos pos, BlockState state, BlockState old) {
        int age = state.getValue(AGE);
        if (!old.is(this) || !Objects.equals(old.getValue(AGE), age) || this.runecraftory$isAtMaxAge(state)) {
            if (age == 0 || old.getOptionalValue(AGE).orElse(0) == 2)
                Growable.super.onGrow(level, pos, state, old);
            if (this.growTree(level, pos, state, level.getRandom())) {
                Growable.super.onGrow(level, pos, state, old);
                level.setBlock(pos.below(), ModBlocks.TREE_SOIL.get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
