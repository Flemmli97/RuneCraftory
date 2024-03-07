package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockFruitTreeLeaf;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
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

    private int health;
    private List<BlockPos> logs = new ArrayList<>();
    private List<BlockPos> leaves = new ArrayList<>();
    private List<BlockPos> fruits = new ArrayList<>();

    public TreeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.TREE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void updateTreeLogs(Collection<BlockPos> pos) {
        // Remove soil block
        this.logs = new ArrayList<>(pos.stream().filter(p -> !p.equals(this.getBlockPos().below())).toList());
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

    public int getHealth() {
        return this.health;
    }

    public void onBreak() {
        this.health = Math.max(0, this.health - 5);
    }

    public void dailyUpdate() {
        this.health = Math.min(MAX_HEALTH, this.health + 3);
    }

    public void update(ServerLevel level) {
        for (BlockPos pos : this.fruits) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof BlockFruitTreeLeaf) {
                level.setBlock(pos, state.setValue(BlockFruitTreeLeaf.HAS_FRUIT, true), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.health = tag.getInt("Health");
        ListTag logs = tag.getList("Logs", Tag.TAG_INT_ARRAY);
        logs.forEach(t -> this.logs.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow(false, RuneCraftory.LOGGER::error)));
        ListTag leaves = tag.getList("Leaves", Tag.TAG_INT_ARRAY);
        leaves.forEach(t -> this.leaves.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow(false, RuneCraftory.LOGGER::error)));
        ListTag fruits = tag.getList("Fruits", Tag.TAG_INT_ARRAY);
        fruits.forEach(t -> this.fruits.add(BlockPos.CODEC.parse(NbtOps.INSTANCE, t).getOrThrow(false, RuneCraftory.LOGGER::error)));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Health", this.health);
        ListTag logs = new ListTag();
        this.logs.forEach(p -> logs.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow(false, RuneCraftory.LOGGER::error)));
        tag.put("Logs", logs);
        ListTag leaves = new ListTag();
        this.leaves.forEach(p -> leaves.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow(false, RuneCraftory.LOGGER::error)));
        tag.put("Leaves", leaves);
        ListTag fruits = new ListTag();
        this.fruits.forEach(p -> fruits.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, p).getOrThrow(false, RuneCraftory.LOGGER::error)));
        tag.put("Fruits", fruits);
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
}