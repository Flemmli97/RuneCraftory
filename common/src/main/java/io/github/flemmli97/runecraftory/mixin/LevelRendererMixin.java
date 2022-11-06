package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    private Minecraft minecraft;

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
        return ClientMixinUtils.p(this.minecraft.level, pos, this.rf4CacheBiome);
    }
}
