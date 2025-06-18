package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.mixin.BiomeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SeasonUtils {

    public static boolean coldEnoughForSnowSeason(LevelReader level, BlockPos pos, Biome biome) {
        if (!GeneralConfig.seasonedSnow)
            return false;
        return seasonBasedTemp(level, pos, biome) < 0.15;
    }

    public static float seasonBasedTemp(LevelReader level, BlockPos pos, Biome biome) {

        float temp = ((BiomeAccessor) (Object) biome).biomeTemp(pos);
        if (!GeneralConfig.seasonedSnow)
            return temp;
        EnumSeason season;
        if (level.isClientSide())
            season = ClientHandlers.CLIENT_CALENDAR.currentSeason();
        else if (level instanceof ServerLevel serverLevel)
            season = WorldHandler.get(serverLevel.getServer()).currentSeason();
        else
            return temp;
        switch (season) {
            case SUMMER -> temp += 0.1f;
            case FALL -> temp -= 0.25f;
            case WINTER -> temp -= 0.8f;
        }
        return temp;
    }

    // Unused atm. TODO: Have a way to mark player placed snow so that doesnt melt
    public static void doSnowMelt(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (GeneralConfig.seasonedSnow && random.nextInt(5) == 0) {
            Biome biome = level.getBiome(pos).value();
            float def = ((BiomeAccessor) (Object) biome).biomeTemp(pos);
            float seasonal = seasonBasedTemp(level, pos, biome);
            if (def != seasonal && seasonal < 0.15) {
                SnowLayerBlock.dropResources(state, level, pos);
                level.removeBlock(pos, false);
            }
        }
    }
}
