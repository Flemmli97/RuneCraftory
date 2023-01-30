package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin {

    @Unique
    private float preFall;

    @ModifyVariable(method = "fallOn", at = @At(value = "HEAD"), argsOnly = true)
    private float stopFallPre(float fallDistance, Level level, BlockState state, BlockPos pos, Entity entity) {
        this.preFall = fallDistance;
        if (EntityCalls.shouldPreventFarmlandTrample(entity, level)) {
            return 0;
        }
        return fallDistance;
    }

    @ModifyVariable(method = "fallOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V", shift = At.Shift.BY, by = -2), argsOnly = true)
    private float stopFallPost(float fallDistance, Level level, BlockState state, BlockPos pos, Entity entity) {
        return this.preFall;
    }
}
