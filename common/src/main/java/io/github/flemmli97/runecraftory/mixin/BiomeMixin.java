package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.flemmli97.runecraftory.common.utils.SeasonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {

    @ModifyExpressionValue(method = "shouldSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z"))
    private boolean onSnowCheck(boolean rain, LevelReader reader, BlockPos pos) {
        return rain && !SeasonUtils.coldEnoughForSnowSeason(reader, pos, (Biome) (Object) this);
    }

    @ModifyExpressionValue(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z"))
    private boolean onFreezeCheck(boolean rain, LevelReader reader, BlockPos pos) {
        return rain && !SeasonUtils.coldEnoughForSnowSeason(reader, pos, (Biome) (Object) this);
    }
}
