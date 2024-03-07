package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {

    @Inject(method = "getAverageGrassColor", at = @At("RETURN"), cancellable = true)
    private static void grass(BlockAndTintGetter level, BlockPos blockPos, CallbackInfoReturnable<Integer> info) {
        if (ClientConfig.GRASS_COLOR)
            info.setReturnValue(ClientMixinUtils.modifyColoredTintGrass(level, info.getReturnValue()));
    }

    @Inject(method = "getAverageFoliageColor", at = @At("RETURN"), cancellable = true)
    private static void foliage(BlockAndTintGetter level, BlockPos blockPos, CallbackInfoReturnable<Integer> info) {
        if (ClientConfig.FOLIAGE_COLOR)
            info.setReturnValue(ClientMixinUtils.modifyColoredTint(level, info.getReturnValue()));
    }
}
