package io.github.flemmli97.runecraftory.common.world;

import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Set;

public class WorldHandler extends SavedData {

    private static final String id = "RCCalendar";

    private final CalendarImpl calendar = new CalendarImpl();

    /**
     * Non persistent tracker to update stuff daily
     */
    private final Set<IDailyUpdate> updateTracker = Sets.newConcurrentHashSet();

    private int updateDelay, lastUpdateDay;

    public WorldHandler() {
    }

    private WorldHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static WorldHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(WorldHandler::new, WorldHandler::new, id);
    }

    public static boolean canUpdateWeather(Level level) {
        return GeneralConfig.modifyWeather && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }

    public static boolean shouldUpdateWeather(Level level, EnumWeather currentWeather) {
        if (currentWeather == EnumWeather.RUNEY || currentWeather == EnumWeather.STORM)
            return WorldUtils.dayTime(level) == 1;
        long time = WorldUtils.dayTime(level);
        return (time % 3000) == 1;
    }

    public CalendarImpl getCalendar() {
        return this.calendar;
    }

    public void setDateDayAndSeason(MinecraftServer server, int date, EnumDay day, EnumSeason season) {
        this.calendar.setDateDayAndSeason(date, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), server);
        this.setDirty();
    }

    public void increaseDay(ServerLevel level) {
        int date = WorldUtils.day(level);
        EnumDay day = EnumDay.values()[Math.floorMod(date, EnumDay.values().length)];
        EnumSeason season = EnumSeason.values()[Math.floorMod(date / 30, EnumSeason.values().length)];
        this.calendar.setDateDayAndSeason(date % 30 + 1, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), level.getServer());
        this.setDirty();
    }

    public EnumSeason currentSeason() {
        return this.calendar.currentSeason();
    }

    public int date() {
        return this.calendar.date();
    }

    public EnumDay currentDay() {
        return this.calendar.currentDay();
    }

    public EnumWeather currentWeather() {
        return this.calendar.currentWeather();
    }

    public EnumWeather[] tomorrowsWeather() {
        return this.calendar.tomorrowsForecast();
    }

    public void update(ServerLevel level) {
        boolean doWeather = canUpdateWeather(level);
        if (WorldUtils.canUpdateDaily(level, this.lastUpdateDay)) {
            this.increaseDay(level);
            this.updateTracker.removeIf(IDailyUpdate::inValid);
            this.updateTracker.forEach(update -> update.update(level));
            this.createDailyWeather(level);
            if (doWeather)
                this.updateWeatherTo(level, this.calendar.getCurrentWeatherFor(level));
            this.lastUpdateDay = WorldUtils.day(level);
            return;
        }
        // Checks if current weather is correct and if not corrects it
        /*
        if (doWeather && shouldUpdateWeather(level, this.currentWeather())) {
            this.updateWeatherTo(level, this.calendar.getCurrentWeatherFor(level));
        }*/
        if (--this.updateDelay <= 0) {
            if (doWeather && !this.isCorrectWeather(level))
                this.setMCWeather(level);
            this.updateDelay = 40;
        }
    }

    public void updateWeatherTo(ServerLevel level, EnumWeather weather) {
        this.calendar.setWeather(level.getServer(), weather);
        this.setMCWeather(level);
        this.updateDelay = 100;
        this.setDirty();
    }

    private void createDailyWeather(ServerLevel level) {
        EnumWeather[] nextWeather = new EnumWeather[8];
        EnumSeason season = this.currentSeason();
        int rainCount = 0;
        for (int i = 0; i < nextWeather.length; i++) {
            float chance = level.random.nextFloat();
            if (i != 0) {
                if (nextWeather[0].wholeDay) {
                    nextWeather[i] = nextWeather[0];
                    return;
                }
            } else {
                float stormAdd = (season == EnumSeason.SUMMER || season == EnumSeason.WINTER) ? 0.04F : 0;
                if (chance < 0.03F)
                    nextWeather[i] = EnumWeather.RUNEY;
                else if (chance < 0.015F + stormAdd)
                    nextWeather[i] = EnumWeather.STORM;
                if (nextWeather[i] != null)
                    return;
            }
            float rainAdd = rainCount > 0 ? 0.5f - (rainCount - 1) * 0.2f : 0;
            if (i < 3)
                rainAdd += season == EnumSeason.SUMMER ? 0.1 : 0.05;
            if (chance < 0.1F + rainAdd) {
                nextWeather[i] = EnumWeather.RAIN;
                rainCount++;
            } else
                nextWeather[i] = EnumWeather.CLEAR;
        }
        this.calendar.updateWeathers(nextWeather);
    }

    private void setMCWeather(ServerLevel level) {
        this.currentWeather().setWeather.accept(level);
    }

    private boolean isCorrectWeather(ServerLevel level) {
        return switch (this.currentWeather()) {
            case RAIN -> level.isRaining();
            case CLEAR, RUNEY, CLOUDY -> !level.isRaining() && !level.isThundering();
            case STORM -> level.isRaining() && level.isThundering();
        };
    }

    public void addToTracker(IDailyUpdate update) {
        this.updateTracker.add(update);
    }

    public boolean removeFromTracker(IDailyUpdate update) {
        return this.updateTracker.remove(update);
    }

    public void load(CompoundTag compoundNBT) {
        this.calendar.read(compoundNBT);
        this.lastUpdateDay = compoundNBT.getInt("LastUpdateDay");
    }

    @Override
    public CompoundTag save(CompoundTag compoundNBT) {
        this.calendar.write(compoundNBT);
        compoundNBT.putInt("LastUpdateDay", this.lastUpdateDay);
        return compoundNBT;
    }
}
