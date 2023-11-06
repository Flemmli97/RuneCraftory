package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.blocks.tile.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixinhelper.LevelSnapshotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class BlockTreeBase extends RotatedPillarBlock implements EntityBlock, Growable {

    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private final Supplier<ConfiguredFeature<?, ?>> stump;
    private final Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree1;
    private final Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree2;
    private final Supplier<Item> seedItem;

    public BlockTreeBase(Properties properties, Supplier<ConfiguredFeature<?, ?>> stump, Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree1,
                         Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree2, Supplier<Item> seedItem) {
        super(properties);
        this.stump = stump;
        this.tree1 = tree1;
        this.tree2 = tree2;
        this.seedItem = seedItem;
    }

    public static boolean isAirOrReplaceable(BlockState state) {
        return state.isAir() || state.getMaterial() == Material.REPLACEABLE_PLANT || state.is(BlockTags.LEAVES);
    }

    public boolean growTree(ServerLevel level, BlockPos pos, BlockState state, Random rand) {
        return switch (state.getValue(AGE)) {
            case 2 -> {
                ((LevelSnapshotHandler) level).getSnapshotHandler().takeSnapshot(null);
                if (level.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                    tree.onRemove(level, false);
                }
                boolean result = this.tree2.get().place(level, level.getChunkSource().getGenerator(), rand, pos);
                ((LevelSnapshotHandler) level).getSnapshotHandler().popSnapshots(result);
                yield result;
            }
            case 1 -> {
                ((LevelSnapshotHandler) level).getSnapshotHandler().takeSnapshot(null);
                if (level.getBlockEntity(pos) instanceof TreeBlockEntity tree) {
                    tree.onRemove(level, false);
                }
                boolean result = this.tree1.get().place(level, level.getChunkSource().getGenerator(), rand, pos);
                ((LevelSnapshotHandler) level).getSnapshotHandler().popSnapshots(result);
                yield result;
            }
            case 0 -> this.stump.get().place(level, level.getChunkSource().getGenerator(), rand, pos);
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
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.seedItem.get());
    }

    @Override
    public int getGrowableMaxAge() {
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
    public BlockState getGrowableStateForAge(BlockState current, int age) {
        int newAge = current.getOptionalValue(AGE).map(i -> Mth.clamp(age - 1, 0, i + 1)).orElse(age - 1);
        return this.defaultBlockState().setValue(AGE, newAge);
    }

    @Override
    public boolean isAtMaxAge(BlockState state) {
        return state.getValue(AGE) == 4;
    }

    @Override
    public void onGrow(ServerLevel level, BlockPos pos, BlockState state, BlockState old) {
        int age = state.getValue(AGE);
        if (!old.is(this) || !Objects.equals(old.getValue(AGE), age) || this.isAtMaxAge(state)) {
            if (age == 0 || old.getOptionalValue(AGE).orElse(0) == 2)
                Growable.super.onGrow(level, pos, state, old);
            if (this.growTree(level, pos, state, level.getRandom())) {
                Growable.super.onGrow(level, pos, state, old);
                level.setBlock(pos.below(), ModBlocks.treeSoil.get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
