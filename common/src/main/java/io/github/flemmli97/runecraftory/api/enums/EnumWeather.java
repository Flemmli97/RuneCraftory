package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

public enum EnumWeather {

    CLEAR(false, "clear", clearWeather()),
    CLOUDY(false, "cloudy", clearWeather()),
    RAIN(false, "rain", rainyWeather()),
    STORM(true, "storm", stormyWeather()),
    RUNEY(true, "runey", clearWeather());

    public static final String PREFIX = "runecraftory.weather.";

    public final boolean wholeDay;
    public final String translation;
    public final Consumer<ServerLevel> setWeather;

    EnumWeather(boolean wholeDay, String key, Consumer<ServerLevel> setWeather) {
        this.wholeDay = wholeDay;
        this.translation = PREFIX + key;
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
