package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

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
}
