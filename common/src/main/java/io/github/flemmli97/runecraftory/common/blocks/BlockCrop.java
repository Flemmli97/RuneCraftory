package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixin.CropBlockAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockCrop extends CropBlock {

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

    public Optional<CropProperties> properties() {
        return Optional.ofNullable(DataPackHandler.SERVER_PACK.cropManager().get(this.seed.get()));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seed.get();
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

    public static void modifyCropDrops(BlockState state, LootContext.Builder builder, CropBlock block, List<ItemStack> list) {
        CropProperties prop = block instanceof BlockCrop crop ? crop.properties().orElse(null) :
                DataPackHandler.SERVER_PACK.cropManager().get(((CropBlockAccessor) block).getSeedItem().asItem());
        if (prop != null) {
            Vec3 pos = builder.getOptionalParameter(LootContextParams.ORIGIN);
            int itemLevel = pos != null ? getCropLevel(builder.getLevel(), new BlockPos(pos)) : 1;
            list.forEach(s -> modifyStack(prop, s, itemLevel));
        }
    }

    private static int getCropLevel(ServerLevel level, BlockPos pos) {
        return FarmlandHandler.get(level.getServer())
                .getData(level, new BlockPos(pos)).map(FarmlandData::getCropLevel).orElse(1);
    }

    private static void modifyStack(CropProperties props, ItemStack stack, int level) {
        if (stack.is(ModTags.SEEDS))
            return;
        stack.setCount(props.maxDrops());
        ItemNBT.getLeveledItem(stack, level);
    }

    public static void harvestCropRightClick(BlockState state, Level level, BlockPos pos, Entity entity, ItemStack stack, CropProperties props, Function<ItemStack, ItemStack> stackConsumer) {
        if (!(level instanceof ServerLevel serverLevel) || !(state.getBlock() instanceof CropBlock cropBlock))
            return;
        if (stackConsumer != null) {
            Block.getDrops(state, serverLevel, pos, null, entity, stack)
                    .forEach(s -> {
                        ItemStack rest = stackConsumer.apply(s);
                        if (!rest.isEmpty())
                            Block.popResource(level, pos, rest);
                    });
            state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY);
        } else
            dropResources(state, level, pos, null, entity, stack);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        if (props != null && props.regrowable()) {
            //Actually handled at block state change detection
            level.setBlock(pos, state.setValue(cropBlock.getAgeProperty(), 0), Block.UPDATE_ALL);
        } else
            level.removeBlock(pos, false);
        if (cropBlock.isMaxAge(state) && entity instanceof ServerPlayer player) {
            spawnRuney(player, pos);
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 2f));
        }
    }

    public static void spawnRuney(ServerPlayer player, BlockPos pos) {
        if (player.getRandom().nextFloat() < GeneralConfig.runeyChance) {
            Entity entity = player.getRandom().nextFloat() < 0.4 ? ModEntities.runey.get().create(player.getLevel()) : ModEntities.statBonus.get().create(player.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            player.level.addFreshEntity(entity);
        }
    }
}