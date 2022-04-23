package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class WorldUtils {

    public static boolean canUpdateDaily(Level world) {
        return world.getGameRules().getRule(GameRules.RULE_DAYLIGHT).get() && dayTime(world) == 1;
    }

    public static int dayTime(Level world) {
        return (int) (world.getDayTime() % 24000);
    }

    public static int dayTimeTotal(Level world) {
        return (int) (world.getDayTime());
    }

    public static long totalTime(Level world) {
        return world.getGameTime();
    }

    public static int day(Level world) {
        return (int) (world.getDayTime() / 24000 % Integer.MAX_VALUE);
    }

}
