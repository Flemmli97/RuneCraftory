package com.flemmli97.runecraftory.common.utils;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class WorldUtils {

    public static boolean canUpdateDaily(World world) {
        return world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).get() && dayTime(world) == 1;
    }

    public static int dayTime(World world) {
        return (int) (world.getDayTime() % 24000);
    }

    public static int dayTimeTotal(World world) {
        return (int) (world.getDayTime());
    }

    public static long totalTime(World world) {
        return world.getGameTime();
    }

    public static int day(World world) {
        return (int) (world.getDayTime() / 24000 % Integer.MAX_VALUE);
    }

}
