package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Allows for having "dummy" setblock calls that gets applied at a later point
 */
public class LevelSetBlockSnapshot {

    private final Map<BlockPos, BlockSnapshot> snapshots = new HashMap<>();
    private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
    private final Level level;
    private boolean takeSnapshot = false;
    private Predicate<BlockState> condition;

    public LevelSetBlockSnapshot(Level level) {
        this.level = level;
    }

    public void takeSnapshot(@Nullable Predicate<BlockState> condition) {
        this.takeSnapshot = true;
        this.condition = condition;
    }

    public boolean isTakingSnapshot() {
        return this.takeSnapshot;
    }

    public void appendBlockSnapshot(BlockSnapshot blockSnapshot) {
        BlockSnapshot current;
        if (this.condition == null || this.condition.test((current = this.snapshots.get(blockSnapshot.pos())) != null ? current.state :
                this.level.getBlockState(blockSnapshot.pos)))
            this.snapshots.put(blockSnapshot.pos(), blockSnapshot);
        if (blockSnapshot.state().getBlock() instanceof EntityBlock entityBlock) {
            BlockEntity entity = entityBlock.newBlockEntity(blockSnapshot.pos(), blockSnapshot.state());
            if (entity != null) {
                BlockEntity currentEntity = this.blockEntities.get(blockSnapshot.pos());
                if (currentEntity != null)
                    currentEntity.setRemoved();
                entity.setLevel(this.level);
                this.blockEntities.put(blockSnapshot.pos(), entity);
            }
        }
    }

    @Nullable
    public BlockState getBlockState(BlockPos pos) {
        BlockSnapshot snapshot = this.snapshots.get(pos);
        if (snapshot != null)
            return snapshot.state();
        return null;
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.blockEntities.get(pos);
    }

    public void popSnapshots(boolean place) {
        this.takeSnapshot = false;
        if (place) {
            this.snapshots.forEach((pos, snapshot) -> this.level.setBlock(snapshot.pos, snapshot.state, snapshot.updateFlag));
            this.blockEntities.forEach((pos, entity) -> {
                CompoundTag tag = entity.saveWithoutMetadata(this.level.registryAccess());
                this.level.getBlockEntity(pos).loadWithComponents(tag, this.level.registryAccess());
            });
        }
        this.snapshots.clear();
        this.blockEntities.clear();
    }

    public record BlockSnapshot(BlockState state, BlockPos pos, int updateFlag) {

    }
}
