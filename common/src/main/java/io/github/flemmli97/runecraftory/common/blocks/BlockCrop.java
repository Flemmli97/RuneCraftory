package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.tile.CropBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockCrop extends BushBlock implements BonemealableBlock, EntityBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty WILTED = BooleanProperty.create("wilted");
    private static final AABB[] CROPS_AABB = new AABB[]{new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};
    private final Supplier<Item> crop;
    private final Supplier<Item> giant;
    private final Supplier<Item> seed;

    public BlockCrop(BlockBehaviour.Properties prop, Supplier<Item> crop, Supplier<Item> giant, Supplier<Item> seed) {
        super(prop);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(WILTED, false));
        this.crop = crop;
        this.giant = giant;
        this.seed = seed;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (this.isMaxAge(state, level, pos)) {
            this.harvestCrop(state, level, pos, player, player.getItemInHand(hand), null);
            if (player instanceof ServerPlayer serverPlayer)
                Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.FARMING, 2f));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void harvestCrop(BlockState state, Level level, BlockPos pos, Entity entity, ItemStack stack, Function<ItemStack, ItemStack> stackConsumer) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (stackConsumer != null) {
            if (level instanceof ServerLevel serverLevel) {
                Block.getDrops(state, serverLevel, pos, tile, entity, stack)
                        .forEach(s -> {
                            ItemStack rest = stackConsumer.apply(s);
                            if (!rest.isEmpty())
                                Block.popResource(level, pos, rest);
                        });
                state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY);
            }
        } else
            dropResources(state, level, pos, tile, entity, stack);
        level.levelEvent(2001, pos, Block.getId(state));
        if (tile instanceof CropBlockEntity && this.properties().map(CropProperties::regrowable).orElse(false)) {
            ((CropBlockEntity) tile).onRegrowableHarvest(this);
        } else
            level.removeBlock(pos, false);
        if (this.isMaxAge(state, level, pos) && entity instanceof ServerPlayer player)
            spawnRuney(player, pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof CropBlockEntity crop && this.properties().map(p -> crop.age() >= p.growth()).orElse(false)
                && !crop.isWilted())
            list.forEach(stack -> this.modifyStack(stack, crop));
        else
            list.clear();
        return list;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    public boolean isMaxAge(BlockState state, Level level, BlockPos pos) {
        return state.getValue(AGE) == 3;
    }

    public Optional<CropProperties> properties() {
        return Optional.ofNullable(DataPackHandler.cropManager().get(this.seed.get()));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModTags.farmland);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && this.mayPlaceOn(level.getBlockState(blockPos), level, blockPos);
    }

    private void modifyStack(ItemStack stack, CropBlockEntity tile) {
        this.properties().ifPresent(prop -> stack.setCount(prop.maxDrops()));
        ItemNBT.getLeveledItem(stack, tile.level());
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockEntity(pos.below()) instanceof FarmBlockEntity farm && farm.canUseBonemeal();
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos.below()) instanceof FarmBlockEntity farm)
            farm.applyBonemeal();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemNBT.getLeveledItem(super.getCloneItemStack(level, pos, state), 1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(WILTED);
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

    public static void spawnRuney(ServerPlayer player, BlockPos pos) {
        if (player.getRandom().nextFloat() < GeneralConfig.runeyChance) {
            Entity entity = player.getRandom().nextFloat() < 0.4 ? ModEntities.runey.get().create(player.getLevel()) : ModEntities.statBonus.get().create(player.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            player.level.addFreshEntity(entity);
        }
    }
}