package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

public enum EnumWeather {

    CLEAR(false, "runecraftory.weather.clear", clearWeather()),
    CLOUDY(false, "runecraftory.weather.cloudy", clearWeather()),
    RAIN(false, "runecraftory.weather.rain", rainyWeather()),
    STORM(true, "runecraftory.weather.storm", stormyWeather()),
    RUNEY(true, "runecraftory.weather.runey", clearWeather());

    public final boolean wholeDay;
    public final Component translation;
    public final Consumer<ServerLevel> setWeather;

    EnumWeather(boolean wholeDay, String translation, Consumer<ServerLevel> setWeather) {
        this.wholeDay = wholeDay;
        this.translation = new TranslatableComponent(translation);
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
