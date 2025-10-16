package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.blocks.util.LazyResolvedRegistryEntry;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class GiantCropBlock extends ExtendedCropBlock {

    public static final MapCodec<GiantCropBlock> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(propertiesCodec(),
                    LazyResolvedRegistryEntry.codec(Registries.ITEM).fieldOf("crop").forGetter(d -> d.crop),
                    LazyResolvedRegistryEntry.codec(Registries.ITEM).fieldOf("seed").forGetter(d -> d.seed),
                    Codec.BOOL.fieldOf("small").forGetter(d -> d.small)
            ).apply(inst, GiantCropBlock::new));

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;

    protected static final List<Pair<BlockPos, Direction>> CROP_POSITION = List.of(
            Pair.of(new BlockPos(0, 0, 0), Direction.NORTH), Pair.of(new BlockPos(0, 1, 0), Direction.NORTH),
            Pair.of(new BlockPos(0, 0, -1), Direction.WEST), Pair.of(new BlockPos(0, 1, -1), Direction.WEST),
            Pair.of(new BlockPos(-1, 0, 0), Direction.EAST), Pair.of(new BlockPos(-1, 1, 0), Direction.EAST),
            Pair.of(new BlockPos(-1, 0, -1), Direction.SOUTH), Pair.of(new BlockPos(-1, 1, -1), Direction.SOUTH));

    private static final VoxelShape[] SHAPE_BOTTOM = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 12.0D));
    private static final VoxelShape[] SHAPE_TOP = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0.0D, 0.0D, 0.0D, 12.0D, 12.0D, 12.0D));
    private static final VoxelShape[] SHAPE_TOP_LESS = VoxelUtils.joinedOrDirs(VoxelUtils.ShapeBuilder.of(0.0D, 0.0D, 0.0D, 12.0D, 5.0D, 12.0D));

    private final boolean small;

    public GiantCropBlock(Properties prop, ResourceKey<Item> giant, ResourceKey<Item> seed) {
        this(prop, giant, seed, false);
    }

    public GiantCropBlock(Properties prop, ResourceKey<Item> giant, ResourceKey<Item> seed, boolean small) {
        super(prop, giant, seed);
        this.small = small;
        this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.NORTH).setValue(HALF, Half.BOTTOM));
    }

    private GiantCropBlock(BlockBehaviour.Properties prop, LazyResolvedRegistryEntry<Item> crop, LazyResolvedRegistryEntry<Item> seed, boolean small) {
        this(prop, crop.getKey(), seed.getKey(), small);
    }

    @Override
    public MapCodec<GiantCropBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == Half.TOP) {
            if (this.small) {
                return SHAPE_TOP_LESS[state.getValue(GiantCropBlock.DIRECTION).get2DDataValue()];
            }
            return SHAPE_TOP[state.getValue(GiantCropBlock.DIRECTION).get2DDataValue()];
        }
        return SHAPE_BOTTOM[state.getValue(GiantCropBlock.DIRECTION).get2DDataValue()];
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return (state.is(this) && state.getValue(HALF) == Half.BOTTOM) || super.mayPlaceOn(state, level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        Direction dir = state.getValue(DIRECTION);
        if (direction == dir) {
            if (!neighborState.is(this) || neighborState.getValue(DIRECTION).getClockWise() != dir)
                return Blocks.AIR.defaultBlockState();
            int neightborAge = neighborState.getValue(AGE);
            if (neightborAge != state.getValue(AGE)) {
                return state.setValue(AGE, neightborAge);
            }
        }
        Half half = state.getValue(HALF);
        Direction fromHalf = half == Half.BOTTOM ? Direction.UP : Direction.DOWN;
        if (fromHalf == direction) {
            if (!neighborState.is(this) || neighborState.getValue(HALF) != (half == Half.BOTTOM ? Half.TOP : Half.BOTTOM))
                return Blocks.AIR.defaultBlockState();
            int neightborAge = neighborState.getValue(AGE);
            if (neightborAge != state.getValue(AGE)) {
                return state.setValue(AGE, neightborAge);
            }
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()
                && (state.getValue(DIRECTION) != Direction.NORTH || state.getValue(HALF) != Half.BOTTOM)) {
            List<Pair<BlockPos, BlockState>> positions = new ArrayList<>();
            AtomicReference<Pair<BlockPos, BlockState>> first = new AtomicReference<>();
            this.applyToCrop(level, pos, state, (p, s) -> {
                if (!p.equals(pos)) {
                    if (s.getValue(DIRECTION) == Direction.NORTH && s.getValue(HALF) == Half.BOTTOM) {
                        first.set(Pair.of(p, s));
                    } else {
                        positions.add(Pair.of(p, s));
                    }
                }
            });
            // Break north bottom one first cause this drops the items.
            // Breaking this first prevents items from being dropped, otherwise due to update order items will still drop
            // If a loot table changes this behaviour that's on them
            if (first.get() != null) {
                positions.addFirst(first.get());
            }
            positions.forEach(p -> {
                level.setBlock(p.getFirst(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, p.getFirst(), Block.getId(p.getSecond()));
            });
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(ExtendedCropBlock.WILTED).add(DIRECTION).add(HALF);
    }

    @Override
    public int getGiantAge() {
        return 0;
    }

    @Override
    public BlockState runecraftory$getGrowableStateForAge(BlockState current, int age) {
        return current.setValue(this.getAgeProperty(), age);
    }

    @Override
    public void onWither(int amount, Level level, BlockState state, BlockPos pos) {
        this.applyToCrop(level, pos, state, (p, s) -> super.onWither(amount, level, s, p));
    }

    @Override
    public void onWater(Level level, BlockPos pos, BlockState state) {
        this.applyToCrop(level, pos, state, (p, s) -> {
            super.onWater(level, p, s);
            BlockPos belowPos = p.below();
            BlockState below = level.getBlockState(belowPos);
            if (below.hasProperty(FarmBlock.MOISTURE))
                level.setBlock(belowPos, below.setValue(FarmBlock.MOISTURE, 7), Block.UPDATE_ALL);
        });
    }

    @Override
    public boolean canGrow(ServerLevel level, BlockPos pos, BlockState state) {
        return state.getValue(HALF) == Half.BOTTOM && state.getValue(DIRECTION) == Direction.NORTH && !this.runecraftory$isAtMaxAge(state);
    }

    @Override
    public BlockPos getCropPosition(Level level, BlockPos pos, BlockState state) {
        BlockPos.MutableBlockPos newPos = pos.mutable();
        Direction dir = state.getValue(DIRECTION);
        while (dir != Direction.NORTH) {
            newPos.move(dir);
            dir = dir.getCounterClockWise();
        }
        if (state.getValue(HALF) == Half.TOP) {
            newPos.move(0, -1, 0);
        }
        BlockState target = level.getBlockState(newPos);
        if (!target.is(this))
            return pos;
        return newPos.immutable();
    }

    private void applyToCrop(Level level, BlockPos start, BlockState current, BiConsumer<BlockPos, BlockState> apply) {
        BlockPos.MutableBlockPos mut = start.mutable();
        Direction dir = current.getValue(DIRECTION);
        Half half = current.getValue(HALF);
        if (half == Half.TOP) {
            start = start.below();
        }
        Rotation rot = EntityUtils.fromDirection(dir);
        BlockState blockAt;
        for (Pair<BlockPos, Direction> pos : CROP_POSITION) {
            BlockPos rotated = pos.getFirst().rotate(rot);
            mut.setWithOffset(start, rotated);
            blockAt = level.getBlockState(mut);
            if (blockAt.is(this)) {
                Direction blockDir = blockAt.getValue(DIRECTION);
                if (blockDir == rot.rotate(pos.getSecond()) && blockAt.getValue(HALF) == (pos.getFirst().getY() == 1 ? Half.TOP : Half.BOTTOM)) {
                    apply.accept(mut.immutable(), blockAt);
                }
            }
        }
    }

    public boolean isGiantOf(BlockState current, BlockState newState) {
        if (current.is(newState.getBlock()))
            return false;
        CropProperties prop = DataPackHandler.INSTANCE.cropManager().get(current.getBlock());
        return prop != null && prop.getGiantVersion().map(b -> b == this).orElse(false);
    }
}