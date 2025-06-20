package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.utils.SeasonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private ClientLevel level;

    @WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation checkSnowAndRain(Biome instance, BlockPos pos, Operation<Biome.Precipitation> origOp) {
        Biome.Precipitation original = origOp.call(instance, pos);
        if (original == Biome.Precipitation.RAIN && SeasonUtils.coldEnoughForSnowSeason(this.minecraft.level, pos, instance))
            return Biome.Precipitation.SNOW;
        return original;
    }

    @WrapOperation(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation onRainTick(Biome instance, BlockPos pos, Operation<Biome.Precipitation> origOp) {
        Biome.Precipitation original = origOp.call(instance, pos);
        if (original == Biome.Precipitation.RAIN && SeasonUtils.coldEnoughForSnowSeason(this.minecraft.level, pos, instance))
            return Biome.Precipitation.SNOW;
        return original;
    }

    @Inject(method = "renderHitOutline", at = @At("RETURN"))
    private void onBlockOutline(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, CallbackInfo info) {
        ClientCalls.onBlockHighlightRender(this.level, poseStack, consumer, entity, camX, camY, camZ, pos, state);
    }
}
