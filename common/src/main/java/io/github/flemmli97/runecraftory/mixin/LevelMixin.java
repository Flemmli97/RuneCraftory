package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.LevelSetBlockSnapshot;
import io.github.flemmli97.runecraftory.mixinhelper.LevelSnapshotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Level.class, priority = 1001)
public abstract class LevelMixin implements LevelSnapshotHandler {

    @Unique
    private final LevelSetBlockSnapshot runecraftory$levelSnapshot = new LevelSetBlockSnapshot((Level) (Object) this);

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"), cancellable = true)
    private void handleSetBlock(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory$levelSnapshot.isTakingSnapshot()) {
            this.runecraftory$levelSnapshot.appendBlockSnapshot(new LevelSetBlockSnapshot.BlockSnapshot(state, pos instanceof BlockPos.MutableBlockPos ? pos.immutable() : pos, flags));
            info.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    private void handleGetBlock(BlockPos pos, CallbackInfoReturnable<BlockState> info) {
        if (this.runecraftory$levelSnapshot.isTakingSnapshot()) {
            BlockState state = this.runecraftory$levelSnapshot.getBlockState(pos);
            if (state != null)
                info.setReturnValue(state);
        }
    }

    @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
    private void handleGetBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> info) {
        if (this.runecraftory$levelSnapshot.isTakingSnapshot()) {
            BlockEntity entity = this.runecraftory$levelSnapshot.getBlockEntity(pos);
            if (entity != null)
                info.setReturnValue(entity);
        }
    }

    @Override
    public LevelSetBlockSnapshot runecraftory$getSnapshotHandler() {
        return this.runecraftory$levelSnapshot;
    }
}
