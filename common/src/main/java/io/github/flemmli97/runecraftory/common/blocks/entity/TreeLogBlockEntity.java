package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TreeLogBlockEntity extends BlockEntity {

    private BlockPos treeBase = BlockPos.ZERO;

    public TreeLogBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.TREE_LOG_ENTITY.get(), blockPos, blockState);
    }

    public void updateTreeRoot(BlockPos pos) {
        this.treeBase = pos;
        this.setChanged();
    }

    public BlockPos treeBase() {
        return this.treeBase;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.treeBase = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("TreeBase")).getOrThrow();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("TreeBase", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, this.treeBase).getOrThrow());
    }
}