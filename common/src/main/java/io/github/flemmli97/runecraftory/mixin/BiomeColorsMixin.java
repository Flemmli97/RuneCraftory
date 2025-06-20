package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {

    @ModifyReturnValue(method = "getAverageGrassColor", at = @At("RETURN"))
    private static int grass(int original, BlockAndTintGetter level, BlockPos blockPos) {
        if (ClientConfig.grassColor)
            return ClientMixinUtils.modifyColoredTintGrass(level, original);
        return original;
    }

    @ModifyReturnValue(method = "getAverageFoliageColor", at = @At("RETURN"))
    private static int foliage(int original, BlockAndTintGetter level, BlockPos blockPos) {
        if (ClientConfig.foliageColor)
            return ClientMixinUtils.modifyColoredTint(level, original);
        return original;
    }
}
