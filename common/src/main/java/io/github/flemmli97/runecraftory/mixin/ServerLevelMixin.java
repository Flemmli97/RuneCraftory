package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Unique
    private Biome rf4BiomeCache;

    @ModifyVariable(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
    private Biome cacheBiome(Biome biome) {
        this.rf4BiomeCache = biome;
        return biome;
    }

    @ModifyArg(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;handlePrecipitation(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/biome/Biome$Precipitation;)V"))
    private Biome.Precipitation withSeason(BlockState state, Level level, BlockPos pos, Biome.Precipitation old) {
        if (GeneralConfig.seasonedSnow && WorldUtils.coldEnoughForSnow(level, pos, this.rf4BiomeCache) && this.rf4BiomeCache.getPrecipitation() != Biome.Precipitation.NONE) {
            BlockPos above = pos.above();
            if (WorldUtils.canPlaceSnowAt(level, above)) {
                level.setBlockAndUpdate(above, ModBlocks.snow.get().defaultBlockState());
            }
            if (old == Biome.Precipitation.RAIN)
                return Biome.Precipitation.SNOW;
        }
        return old;
    }
}
