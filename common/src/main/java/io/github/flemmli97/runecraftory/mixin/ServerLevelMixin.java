package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.ChunkSnowData;
import io.github.flemmli97.runecraftory.mixinhelper.LevelChunkTick;
import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Unique
    private Biome runecraftoryBiomeCache;

    @Inject(method = "tickChunk", at = @At(value = "HEAD"))
    private void onFirstChunkTick(LevelChunk chunk, int randomTickSpeed, CallbackInfo info) {
        if (((LevelChunkTick) chunk).getAndSetFirstTick())
            ChunkSnowData.get((ServerLevel) (Object) this).onChunkLoad((ServerLevel) (Object) this, chunk);
    }

    @ModifyVariable(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
    private Biome cacheBiome(Biome biome) {
        this.runecraftoryBiomeCache = biome;
        return biome;
    }

    @ModifyArg(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;handlePrecipitation(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/biome/Biome$Precipitation;)V"))
    private Biome.Precipitation withSeason(BlockState state, Level level, BlockPos pos, Biome.Precipitation old) {
        if (GeneralConfig.seasonedSnow && WorldUtils.coldEnoughForSnow(level, pos, this.runecraftoryBiomeCache)) {
            BlockPos above = pos.above();
            if (WorldUtils.canPlaceSnowAt(level, above)) {
                level.setBlockAndUpdate(above, ModBlocks.snow.get().defaultBlockState());
            }
            if (old == Biome.Precipitation.RAIN)
                return Biome.Precipitation.SNOW;
        }
        return old;
    }

    @Inject(method = "onBlockStateChange", at = @At("RETURN"))
    private void stateChangeInject(BlockPos pos, BlockState blockState, BlockState newState, CallbackInfo info) {
        MixinUtils.onBlockStateChange((ServerLevel) (Object) this, pos, blockState, newState);
    }
}
