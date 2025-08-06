package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.blocks.TreeFruitLeavesBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeLeavesBlock;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandDataContainer;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeBlockEntity extends BlockEntity {

    private final PositionHolder logs = new PositionHolder();
    private final PositionHolder leaves = new PositionHolder();
    private final PositionHolder fruits = new PositionHolder();

    private boolean withered;

    public TreeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.TREE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void updateTreeLogs(BlockGetter level, Collection<BlockPos> pos) {
        // Remove soil block
        List<BlockPos> list = new ArrayList<>(pos.stream().filter(p -> !p.equals(this.getBlockPos().below())).toList());
        list.forEach(log -> {
            if (level.getBlockEntity(log) instanceof TreeLogBlockEntity logBlockEntity) {
                logBlockEntity.updateTreeRoot(this.getBlockPos());
            }
        });
        this.logs.scheduleNewPositions(list);
        this.setChanged();
    }

    public void updateTreeLeaves(LevelAccessor level, Collection<BlockPos> pos) {
        this.leaves.scheduleNewPositions(pos);
        this.witherTree(level, this.withered, 0);
        this.setChanged();
    }

    public void updateTreeFruits(LevelAccessor level, Collection<BlockPos> pos) {
        this.fruits.scheduleNewPositions(pos);
        this.witherTree(level, this.withered, 1);
        this.setChanged();
    }

    public void invalidateUpdate() {
        this.logs.invalidateUpdate();
        this.leaves.invalidateUpdate();
        this.fruits.invalidateUpdate();
    }

    /**
     * @return True if any of the logs from this tree was broken by any other sources
     * The tree then will stop growing
     */
    public boolean isTreeValid(BlockGetter getter) {
        for (BlockPos pos : this.logs.getPositions()) {
            BlockEntity entity = getter.getBlockEntity(pos);
            if (!(entity instanceof TreeLogBlockEntity log) || !log.treeBase().equals(this.getBlockPos()))
                return false;
        }
        return true;
    }

    public boolean isPartOf(BlockPos pos) {
        return this.logs.getPositions().contains(pos);
    }

    public int getHealth() {
        if (this.getLevel() == null)
            return -1;
        if (this.getLevel().isClientSide) {
            FarmlandDataContainer data = ClientFarmlandHandler.INSTANCE.getData(this.getBlockPos().below());
            return data == null ? 0 : data.health();
        }
        ServerLevel serverLevel = (ServerLevel) this.getLevel();
        return FarmlandHandler.get(serverLevel.getServer())
                .getData(serverLevel, this.getBlockPos().below())
                .map(FarmlandData::getHealth).orElse(0);
    }

    public void onBreak(int cost) {
        if (!(this.getLevel() instanceof ServerLevel serverLevel))
            return;
        FarmlandHandler.get(serverLevel.getServer())
                .getData(serverLevel, this.getBlockPos().below())
                .ifPresent(d -> {
                    d.modifyHealth(serverLevel, -cost);
                    if (d.getHealth() <= 0) {
                        this.onRemove(serverLevel, this.getBlockPos(), true);
                    }
                });
    }

    public void update(ServerLevel level) {
        for (BlockPos pos : this.fruits.getPositions()) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof TreeFruitLeavesBlock) {
                level.setBlock(pos, state.setValue(TreeFruitLeavesBlock.HAS_FRUIT, true), Block.UPDATE_ALL);
            }
        }
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        this.setChanged();
    }

    public void onRemove(Level level, BlockPos source, boolean particle) {
        List<BlockPos> logs = this.logs.getPositions();
        List<BlockPos> leaves = this.leaves.getPositions();
        List<BlockPos> fruits = this.fruits.getPositions();
        this.logs.scheduleNewPositions(List.of());
        this.leaves.scheduleNewPositions(List.of());
        this.fruits.scheduleNewPositions(List.of());
        logs.forEach(p -> this.removeBlock(level, p, p.equals(source) ? 2 : particle ? 1 : 0));
        leaves.forEach(p -> this.removeBlock(level, p, p.equals(source) ? 2 : particle ? 1 : 0));
        fruits.forEach(p -> this.removeBlock(level, p, p.equals(source) ? 2 : particle ? 1 : 0));
        if (!this.getBlockPos().equals(source))
            this.removeBlock(level, this.getBlockPos(), 2);
        this.setChanged();
    }

    private void removeBlock(Level level, BlockPos pos, int removeFlag) {
        switch (removeFlag) {
            case 2 -> level.destroyBlock(pos, true);
            case 1 -> {
                BlockState blockState = level.getBlockState(pos);
                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
                level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
            }
            default -> {
                BlockState blockState = level.getBlockState(pos);
                level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ListTag logs = tag.getList("Logs", Tag.TAG_INT_ARRAY);
        logs.forEach(t -> this.logs.getPositions().add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
        ListTag leaves = tag.getList("Leaves", Tag.TAG_INT_ARRAY);
        leaves.forEach(t -> this.leaves.getPositions().add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
        ListTag fruits = tag.getList("Fruits", Tag.TAG_INT_ARRAY);
        fruits.forEach(t -> this.fruits.getPositions().add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
        this.withered = tag.getBoolean("Withered");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag logs = new ListTag();
        this.logs.getPositions().forEach(p -> logs.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Logs", logs);
        ListTag leaves = new ListTag();
        this.leaves.getPositions().forEach(p -> leaves.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Leaves", leaves);
        ListTag fruits = new ListTag();
        this.fruits.getPositions().forEach(p -> fruits.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Fruits", fruits);
        tag.putBoolean("Withered", this.withered);
    }

    public void witherTree(LevelAccessor level, boolean wither) {
        this.witherTree(level, wither, 2);
    }

    private void witherTree(LevelAccessor level, boolean wither, int positions) {
        if (positions == 2 || positions == 0) {
            this.leaves.getPositions().forEach(p -> {
                BlockState state = level.getBlockState(p);
                if (state.hasProperty(TreeLeavesBlock.WILTED) && state.getValue(TreeLeavesBlock.WILTED) != wither)
                    level.setBlock(p, state.setValue(TreeLeavesBlock.WILTED, wither), Block.UPDATE_ALL);
            });
        }
        if (positions == 2 || positions == 1) {
            this.fruits.getPositions().forEach(p -> {
                BlockState state = level.getBlockState(p);
                if (state.hasProperty(TreeLeavesBlock.WILTED) && state.getValue(TreeLeavesBlock.WILTED) != wither)
                    level.setBlock(p, state.setValue(TreeLeavesBlock.WILTED, wither), Block.UPDATE_ALL);
            });
        }
        this.withered = wither;
    }

    public boolean withered() {
        return this.withered;
    }

    /**
     * Tree grow attempt is lazy evaluated and can happen during block snapshot process.
     * During that the state of this block entity should not change if the tree failed to grow.
     * This struct makes it easy to invalidate the updated positions
     */
    private static class PositionHolder {

        private Collection<BlockPos> toUpdate;
        private List<BlockPos> positions = new ArrayList<>();

        public void invalidateUpdate() {
            this.toUpdate = null;
        }

        public void scheduleNewPositions(Collection<BlockPos> update) {
            this.getPositions();
            this.toUpdate = update;
        }

        public List<BlockPos> getPositions() {
            if (this.toUpdate != null)
                this.positions = new ArrayList<>(this.toUpdate);
            return this.positions;
        }
    }
}