package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.BlockPlaceCtxHelper;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TendCropsGoal extends Goal {

    private static final Function<ItemStack, Boolean> seedItem = s -> !s.isEmpty() && s.getItem() instanceof BlockItem
            && (s.is(ModTags.SEEDS) || s.getItem() == Items.POTATO || s.getItem() == Items.CARROT);

    private final List<BlockPos> toTend = new ArrayList<>();
    private BlockPos selected;

    protected final BaseMonster entity;

    private int checkCooldown;
    private int cooldown;

    private boolean canPlant;

    public TendCropsGoal(BaseMonster entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (cantTendToCropsAnymore(this.entity) || !this.entity.hasRestriction() || --this.checkCooldown > 0)
            return false;
        this.toTend.clear();
        this.canPlant = this.entity.getSeedInventory() != null && Platform.INSTANCE.matchingInventory(
                this.entity.level.getBlockEntity(this.entity.getSeedInventory()), seedItem);
        BlockPos center = this.entity.getRestrictCenter();
        BlockPos.MutableBlockPos mutable = this.entity.getRestrictCenter().mutable();
        int radius = MobConfig.farmRadius;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    mutable.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (this.validPos(mutable, this.entity.level))
                        this.toTend.add(new BlockPos(mutable));
                }
            }
        }
        if (this.toTend.isEmpty()) {
            this.checkCooldown = 40;
            return false;
        }
        this.selected = this.toTend.remove(this.entity.getRandom().nextInt(this.toTend.size()));
        return true;
    }

    private boolean validPos(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CropBlock crop && crop.isMaxAge(state))
            return true;
        BlockState state2 = level.getBlockState(pos.below());
        if (state2.is(ModTags.FARMLAND) && state2.getValue(FarmBlock.MOISTURE) < 7)
            return true;
        if (state2.getBlock() instanceof FarmBlock) {
            if (state.is(ModTags.MONSTER_CLEARABLE))
                return true;
            if (this.canPlant)
                return state.isAir();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.toTend.isEmpty();
    }

    @Override
    public void stop() {
        super.stop();
        this.toTend.clear();
        this.selected = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (cantTendToCropsAnymore(this.entity)) {
            this.toTend.clear();
            return;
        }
        if (--this.cooldown > 0) {
            return;
        }
        if (this.selected == null) {
            if (this.toTend.isEmpty())
                return;
            this.selected = this.toTend.remove(this.entity.getRandom().nextInt(this.toTend.size()));
            this.canPlant = this.entity.getSeedInventory() != null && Platform.INSTANCE.matchingInventory(
                    this.entity.level.getBlockEntity(this.entity.getSeedInventory()), seedItem);
            if (!this.validPos(this.selected, this.entity.level)) {
                this.selected = null;
                this.cooldown = 10;
                return;
            }
        }
        if (!this.selected.closerToCenterThan(this.entity.position(), Math.max(1.1, this.entity.getBbWidth() * 1.9))) {
            Vec3 to = Vec3.atCenterOf(this.selected);
            Path path = this.entity.getNavigation().createPath(to.x(), to.y(), to.z(), 0);
            this.entity.getNavigation().moveTo(path, 1);
            this.cooldown = this.entity.getRandom().nextInt(5) + 5;
        } else {
            BlockState state = this.entity.level.getBlockState(this.selected);
            Block block = state.getBlock();
            boolean success = false;
            if (state.is(ModTags.MONSTER_CLEARABLE)) {
                this.breakBlock((ServerLevel) this.entity.level, this.selected, this.entity.getCropInventory() != null ?
                        s -> Platform.INSTANCE.insertInto(this.entity.level.getBlockEntity(this.entity.getCropInventory()), s) : null);
            } else if (block instanceof CropBlock crop && crop.isMaxAge(state)) {
                this.breakBlock((ServerLevel) this.entity.level, this.selected, this.entity.getCropInventory() != null ?
                        s -> Platform.INSTANCE.insertInto(this.entity.level.getBlockEntity(this.entity.getCropInventory()), s) : null);
                CropProperties props = DataPackHandler.SERVER_PACK.cropManager().get(crop.getCloneItemStack(this.entity.level, this.selected, state).getItem());
                if (props != null && props.regrowable()) {
                    this.entity.level.setBlock(this.selected, crop.getStateForAge(0), Block.UPDATE_ALL);
                }
                success = true;
            } else if (block instanceof BlockCrop crop && crop.isMaxAge(state)) {
                BlockCrop.harvestCropRightClick(state, this.entity.level, this.selected, this.entity, ItemStack.EMPTY, crop.properties().orElse(null), this.entity.getCropInventory() != null ?
                        s -> Platform.INSTANCE.insertInto(this.entity.level.getBlockEntity(this.entity.getCropInventory()), s) : null);
                this.entity.level.getEntities(EntityTypeTest.forClass(ItemEntity.class), this.entity.getBoundingBox().inflate(0.2), e -> true);
                success = true;
            } else {
                BlockPos pos = this.selected.below();
                BlockState state2 = this.entity.level.getBlockState(pos);
                if (state2.is(ModTags.FARMLAND)) {
                    if (state2.getValue(FarmBlock.MOISTURE) < 7) {
                        FarmlandHandler.waterLand((ServerLevel) this.entity.level, pos, state2);
                        success = true;
                    } else if (state.isAir()) {
                        if (this.entity.getSeedInventory() != null) {
                            ItemStack stack = Platform.INSTANCE.findMatchingItem(this.entity.level.getBlockEntity(this.entity.getSeedInventory()), seedItem, 1);
                            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                                blockItem.place(BlockPlaceCtxHelper.entityPlaceAt(this.entity.level, stack, this.selected, Direction.UP));
                                //this.entity.level.setBlock(this.selected, blockItem.getBlock().defaultBlockState(), 3);
                                //this.entity.level.playSound(null, this.selected.getX(), this.selected.getY(), this.selected.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
                                //stack.shrink(1);
                                success = true;
                            }
                        }
                    }
                }
            }
            if (success) {
                this.entity.setHealth(this.entity.getHealth() - 1);
                this.entity.addXp(5 + this.entity.getRandom().nextInt(5));
                this.entity.playInteractionAnimation();
            }
            this.selected = null;
            this.cooldown = this.entity.getRandom().nextInt(15) + 20;
        }
    }

    private void breakBlock(ServerLevel level, BlockPos pos, Function<ItemStack, ItemStack> stackConsumer) {
        BlockState state = level.getBlockState(pos);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (stackConsumer != null) {
            Block.getDrops(state, level, pos, blockEntity, this.entity, ItemStack.EMPTY)
                    .forEach(s -> {
                        ItemStack rest = stackConsumer.apply(s);
                        if (!rest.isEmpty())
                            Block.popResource(level, pos, rest);
                    });
            state.spawnAfterBreak(level, pos, ItemStack.EMPTY);
        } else
            Block.dropResources(state, level, pos, blockEntity, this.entity, ItemStack.EMPTY);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    public static boolean cantTendToCropsAnymore(BaseMonster monster) {
        return Mth.floor(monster.getHealth()) <= Math.max(1, monster.getMaxHealth() * 0.05);
    }
}
