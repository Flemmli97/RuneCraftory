package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.utils.BlockPlaceCtxHelper;
import io.github.flemmli97.runecraftory.common.utils.CropUtils;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TendCrops<E extends BaseMonster> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(1)
            .hasMemory(RuneCraftoryMemoryTypes.FARMING.get());

    private static final Predicate<ItemStack> SEED_ITEM = s -> !s.isEmpty() && s.getItem() instanceof BlockItem
            && (s.is(RunecraftoryTags.Items.SEEDS) || s.getItem() == Items.POTATO || s.getItem() == Items.CARROT);

    public static boolean cantTendToCropsAnymore(BaseMonster monster) {
        return Mth.floor(monster.getHealth()) <= Math.max(1, monster.getMaxHealth() * 0.05);
    }

    private final List<BlockPos> toTend = new ArrayList<>();
    private BlockPos selected;
    private int cooldown;

    private boolean canPlant;

    public TendCrops() {
        this.cooldownFor(e -> 40);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return !cantTendToCropsAnymore(entity) && entity.hasRestriction();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return !this.toTend.isEmpty();
    }

    @Override
    protected void start(E entity) {
        this.toTend.clear();
        this.canPlant = entity.getSeedInventory() != null && Platform.INSTANCE.matchingInventory(
                entity.level().getBlockEntity(entity.getSeedInventory()), SEED_ITEM);
        BlockPos center = entity.getRestrictCenter();
        BlockPos.MutableBlockPos mutable = entity.getRestrictCenter().mutable();
        int radius = MobConfig.farmRadius;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    mutable.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (this.validPos(mutable, entity.level()))
                        this.toTend.add(new BlockPos(mutable));
                }
            }
        }
        if (!this.toTend.isEmpty())
            this.selected = this.toTend.remove(entity.getRandom().nextInt(this.toTend.size()));
    }

    private boolean validPos(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CropBlock crop && crop.isMaxAge(state))
            return true;
        BlockState state2 = level.getBlockState(pos.below());
        if (state2.is(RunecraftoryTags.Blocks.FARMLAND) && state2.getValue(FarmBlock.MOISTURE) < 7)
            return true;
        if (state2.getBlock() instanceof FarmBlock) {
            if (state.is(RunecraftoryTags.Blocks.MONSTER_CLEARABLE))
                return true;
            if (this.canPlant)
                return state.isAir();
        }
        return false;
    }

    @Override
    protected void tick(E entity) {
        if (entity.getAnimationHandler().hasAnimation() || --this.cooldown > 0) {
            return;
        }
        if (this.selected == null) {
            if (this.toTend.isEmpty())
                return;
            this.selected = this.toTend.remove(entity.getRandom().nextInt(this.toTend.size()));
            this.canPlant = entity.getSeedInventory() != null && Platform.INSTANCE.matchingInventory(
                    entity.level().getBlockEntity(entity.getSeedInventory()), SEED_ITEM);
            if (!this.validPos(this.selected, entity.level())) {
                this.selected = null;
                this.cooldown = 10;
                return;
            }
        }
        if (!this.selected.closerToCenterThan(entity.position(), Math.max(1.1, entity.getBbWidth() * 1.9))) {
            Vec3 to = Vec3.atCenterOf(this.selected);
            Path path = entity.getNavigation().createPath(to.x(), to.y(), to.z(), 0);
            entity.getNavigation().moveTo(path, 1);
            this.cooldown = entity.getRandom().nextInt(5) + 5;
        } else {
            BlockPos selected = this.selected;
            BlockState state = entity.level().getBlockState(selected);
            Block block = state.getBlock();
            Runnable run = () -> {
                boolean success = false;
                if (state.is(RunecraftoryTags.Blocks.MONSTER_CLEARABLE)) {
                    breakBlock(entity, selected, entity.getCropInventory() != null ?
                            s -> Platform.INSTANCE.insertInto(entity.level().getBlockEntity(entity.getCropInventory()), s) : null);
                } else if (block instanceof CropBlock crop && crop.isMaxAge(state)) {
                    CropUtils.harvestCropRightClick(state, entity.level(), selected, entity, ItemStack.EMPTY, CropUtils.getPropertiesFor(crop), InteractionHand.MAIN_HAND, entity.getCropInventory() != null ?
                            s -> Platform.INSTANCE.insertInto(entity.level().getBlockEntity(entity.getCropInventory()), s) : null);
                    entity.level().getEntities(EntityTypeTest.forClass(ItemEntity.class), entity.getBoundingBox().inflate(0.2), e -> true);
                    success = true;
                } else {
                    BlockPos pos = selected.below();
                    BlockState state2 = entity.level().getBlockState(pos);
                    if (state2.is(RunecraftoryTags.Blocks.FARMLAND)) {
                        if (state2.getValue(FarmBlock.MOISTURE) < 7) {
                            FarmlandHandler.waterLand((ServerLevel) entity.level(), pos, state2);
                            success = true;
                        } else if (state.isAir()) {
                            if (entity.getSeedInventory() != null) {
                                ItemStack stack = Platform.INSTANCE.findMatchingItem(entity.level().getBlockEntity(entity.getSeedInventory()), SEED_ITEM, 1);
                                if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                                    blockItem.place(BlockPlaceCtxHelper.entityPlaceAt(entity.level(), stack, selected, Direction.UP));
                                    entity.level().setBlock(selected, blockItem.getBlock().defaultBlockState(), 3);
                                    entity.level().playSound(null, selected.getX(), selected.getY(), selected.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
                                    stack.shrink(1);
                                    success = true;
                                }
                            }
                        }
                    }
                }
                if (success) {
                    entity.setHealth(entity.getHealth() - 1);
                    entity.addXp(5 + entity.getRandom().nextInt(5));
                }
            };
            entity.runInteractHandling(run);
            this.selected = null;
            this.cooldown = entity.getRandom().nextInt(15) + 20;
        }
    }

    @Override
    public void stop(E entity) {
        super.stop(entity);
        this.toTend.clear();
        this.selected = null;
    }

    private static <E extends Entity> void breakBlock(E entity, BlockPos pos, Function<ItemStack, ItemStack> stackConsumer) {
        ServerLevel level = (ServerLevel) entity.level();
        BlockState state = level.getBlockState(pos);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (stackConsumer != null) {
            Block.getDrops(state, level, pos, blockEntity, entity, ItemStack.EMPTY)
                    .forEach(s -> {
                        ItemStack rest = stackConsumer.apply(s);
                        if (!rest.isEmpty())
                            Block.popResource(level, pos, rest);
                    });
            state.spawnAfterBreak(level, pos, ItemStack.EMPTY, false);
        } else
            Block.dropResources(state, level, pos, blockEntity, entity, ItemStack.EMPTY);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }
}
