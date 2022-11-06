package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.mixin.BiomeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

public class WorldUtils {

    public static boolean canUpdateDaily(Level level) {
        return level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).get() && dayTime(level) == 1;
    }

    public static int dayTime(Level level) {
        return (int) (level.getDayTime() % 24000);
    }

    public static int dayTimeTotal(Level level) {
        return (int) (level.getDayTime());
    }

    public static long totalTime(Level level) {
        return level.getGameTime();
    }

    public static int day(Level level) {
        return day(level, 0);
    }

    public static int day(Level world, int tickOffset) {
        return (int) ((world.getDayTime() + tickOffset) / 24000 % Integer.MAX_VALUE);
    }

    public static boolean canPlaceSnowAt(Level level, BlockPos pos) {
        return pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, pos) < 10 && level.getBlockState(pos).isAir() && Blocks.SNOW.defaultBlockState().canSurvive(level, pos);
    }

    public static boolean coldEnoughForSnow(Level level, BlockPos pos, Biome biome) {
        if (biome.coldEnoughToSnow(pos)) {
            return false;
        }
        return seasonBasedTemp(level, pos, biome) < 0.15;
    }

    public static float seasonBasedTemp(Level level, BlockPos pos, Biome biome) {
        float temp = ((BiomeAccessor) (Object) biome).biomeTemp(pos);
        if (!GeneralConfig.seasonedSnow)
            return temp;
        EnumSeason season;
        if (level instanceof ServerLevel serverLevel)
            season = WorldHandler.get(serverLevel.getServer()).currentSeason();
        else
            season = ClientHandlers.clientCalendar.currentSeason();
        switch (season) {
            case SUMMER -> temp += 0.1f;
            case FALL -> temp -= 0.25f;
            case WINTER -> temp -= 0.8f;
        }
        return temp;
    }
}
