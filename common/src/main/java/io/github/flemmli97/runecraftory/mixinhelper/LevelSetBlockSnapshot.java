package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
    }

    @Nullable
    public BlockState getBlockState(BlockPos pos) {
        BlockSnapshot snapshot = this.snapshots.get(pos);
        if (snapshot != null)
            return snapshot.state();
        return null;
    }

    public void popSnapshots(boolean place) {
        this.takeSnapshot = false;
        if (place)
            this.snapshots.forEach((k, s) -> this.level.setBlock(s.pos, s.state, s.updateFlag));
        this.snapshots.clear();
    }

    public record BlockSnapshot(BlockState state, BlockPos pos, int updateFlag) {
    }
}
