package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.blocks.FruitTreeLeafBlock;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeBlockEntity extends BlockEntity {

    private static final int MAX_HEALTH = 50;

    private List<BlockPos> logs = new ArrayList<>();
    private List<BlockPos> leaves = new ArrayList<>();
    private List<BlockPos> fruits = new ArrayList<>();

    public TreeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.TREE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void updateTreeLogs(BlockGetter level, Collection<BlockPos> pos) {
        // Remove soil block
        this.logs = new ArrayList<>(pos.stream().filter(p -> !p.equals(this.getBlockPos().below())).toList());
        this.logs.forEach(log -> {
            if (level.getBlockEntity(log) instanceof TreeLogBlockEntity logBlockEntity) {
                logBlockEntity.updateTreeRoot(this.getBlockPos());
            }
        });
        this.setChanged();
    }

    public void updateTreeLeaves(Collection<BlockPos> pos) {
        this.leaves = new ArrayList<>(pos);
        this.setChanged();
    }

    public void updateTreeFruits(Collection<BlockPos> pos) {
        this.fruits = new ArrayList<>(pos);
        this.setChanged();
    }

    /**
     * @return True if any of the logs from this tree was broken by any other sources
     * The tree then will stop growing
     */
    public boolean isTreeValid(BlockGetter getter) {
        for (BlockPos pos : this.logs) {
            BlockEntity entity = getter.getBlockEntity(pos);
            if (!(entity instanceof TreeLogBlockEntity log) || !log.treeBase().equals(this.getBlockPos()))
                return false;
        }
        return true;
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

    public void onBreak() {
        if (!(this.getLevel() instanceof ServerLevel serverLevel))
            return;
        FarmlandHandler.get(serverLevel.getServer())
                .getData(serverLevel, this.getBlockPos().below())
                .ifPresent(d -> {
                    d.modifyHealth(serverLevel, -5);
                    if (d.getHealth() <= 0) {
                        this.onRemove(serverLevel, true);
                        serverLevel.destroyBlock(this.getBlockPos(), true);
                    }
                });
    }

    public void update(ServerLevel level) {
        for (BlockPos pos : this.fruits) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof FruitTreeLeafBlock) {
                level.setBlock(pos, state.setValue(FruitTreeLeafBlock.HAS_FRUIT, true), Block.UPDATE_ALL);
            }
        }
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        this.setChanged();
    }

    public void onRemove(Level level, boolean particle) {
        this.logs.forEach(p -> this.removeBlock(level, p, particle));
        this.leaves.forEach(p -> this.removeBlock(level, p, particle));
        this.fruits.forEach(p -> this.removeBlock(level, p, particle));
    }

    private void removeBlock(Level level, BlockPos pos, boolean particle) {
        BlockState blockState = level.getBlockState(pos);
        if (particle)
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ListTag logs = tag.getList("Logs", Tag.TAG_INT_ARRAY);
        logs.forEach(t -> this.logs.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
        ListTag leaves = tag.getList("Leaves", Tag.TAG_INT_ARRAY);
        leaves.forEach(t -> this.leaves.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
        ListTag fruits = tag.getList("Fruits", Tag.TAG_INT_ARRAY);
        fruits.forEach(t -> this.fruits.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow()));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag logs = new ListTag();
        this.logs.forEach(p -> logs.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Logs", logs);
        ListTag leaves = new ListTag();
        this.leaves.forEach(p -> leaves.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Leaves", leaves);
        ListTag fruits = new ListTag();
        this.fruits.forEach(p -> fruits.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow()));
        tag.put("Fruits", fruits);
    }
}