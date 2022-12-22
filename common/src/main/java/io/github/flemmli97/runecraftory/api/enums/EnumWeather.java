package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

public enum EnumWeather {

    CLEAR(false, clearWeather()),
    CLOUDY(false, clearWeather()),
    RAIN(false, rainyWeather()),
    STORM(true, stormyWeather()),
    RUNEY(true, clearWeather());

    public final boolean wholeDay;
    public final Consumer<ServerLevel> setWeather;

    EnumWeather(boolean wholeDay, Consumer<ServerLevel> setWeather) {
        this.wholeDay = wholeDay;
        this.setWeather = setWeather;
    }

    private static Consumer<ServerLevel> clearWeather() {
        return level -> level.setWeatherParameters(24000, 0, false, false);
    }

    private static Consumer<ServerLevel> rainyWeather() {
        return level -> level.setWeatherParameters(0, 24000, true, false);
    }

    private static Consumer<ServerLevel> stormyWeather() {
        return level -> level.setWeatherParameters(0, 24000, true, true);
    }

}
