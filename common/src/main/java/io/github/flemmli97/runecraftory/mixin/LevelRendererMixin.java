package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private Minecraft minecraft;
    @Shadow
    private ClientLevel level;

    @Unique
    private Biome rf4CacheBiome;

    @ModifyVariable(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z"))
    private Biome cacheBiomeSnowRain(Biome biome) {
        this.rf4CacheBiome = biome;
        return biome;
    }

    @ModifyVariable(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z"))
    private BlockPos.MutableBlockPos snowRainCheck(BlockPos.MutableBlockPos pos) {
        if (WorldUtils.coldEnoughForSnow(this.minecraft.level, pos, this.rf4CacheBiome))
            pos.set(pos.getX(), 1000, pos.getZ());
        return pos;
    }

    @ModifyVariable(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I", ordinal = 0, shift = At.Shift.BEFORE))
    private Biome cacheBiomeTickRain(Biome biome) {
        this.rf4CacheBiome = biome;
        return biome;
    }

    @ModifyVariable(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I", ordinal = 0, shift = At.Shift.BEFORE), ordinal = 2)
    private BlockPos rainTickCheck(BlockPos pos) {
        if (WorldUtils.coldEnoughForSnow(this.minecraft.level, pos, this.rf4CacheBiome))
            return new BlockPos(pos.getX(), this.minecraft.level.getMinBuildHeight() - 1, pos.getZ());
        return pos;
    }

    @Inject(method = "renderHitOutline", at = @At("RETURN"))
    private void onBlockOutline(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, CallbackInfo info) {
        ClientCalls.onBlockHighlightRender(this.level, poseStack, consumer, entity, camX, camY, camZ, pos, state);
    }
}
