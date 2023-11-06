package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.LevelSetBlockSnapshot;
import io.github.flemmli97.runecraftory.mixinhelper.LevelSnapshotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin implements LevelSnapshotHandler {

    @Unique
    private final LevelSetBlockSnapshot runecraftory_level_snapshot = new LevelSetBlockSnapshot((Level) (Object) this);

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"), cancellable = true)
    private void handleSetBlock(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory_level_snapshot.isTakingSnapshot()) {
            this.runecraftory_level_snapshot.appendBlockSnapshot(new LevelSetBlockSnapshot.BlockSnapshot(state, pos instanceof BlockPos.MutableBlockPos ? pos.immutable() : pos, flags));
            info.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.BY, by = -2), cancellable = true)
    private void handleGetBlock(BlockPos pos, CallbackInfoReturnable<BlockState> info) {
        if (this.runecraftory_level_snapshot.isTakingSnapshot()) {
            BlockState state = this.runecraftory_level_snapshot.getBlockState(pos);
            if (state != null)
                info.setReturnValue(state);
        }
    }

    @Override
    public LevelSetBlockSnapshot getSnapshotHandler() {
        return this.runecraftory_level_snapshot;
    }
}
