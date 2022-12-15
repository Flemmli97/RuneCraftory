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

    private int updateDelay;

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
        return time == 1 || time == 6001 || time == 12001 || time == 18001;
    }

    public static boolean canChangeToRuneyOrStormyWeather(Level level) {
        return WorldUtils.dayTime(level) == 1;
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

    public EnumWeather nextWeather() {
        return this.calendar.nextWeather();
    }

    public void update(ServerLevel level) {
        boolean doWeather = canUpdateWeather(level);
        if (doWeather && shouldUpdateWeather(level, this.currentWeather())) {
            this.updateWeatherTo(level, this.nextWeather());
        }
        if (--this.updateDelay <= 0) {
            if (doWeather && !this.isCorrectWeather(level))
                this.setMCWeather(level);
            this.updateDelay = 40;
        }
        if (WorldUtils.canUpdateDaily(level)) {
            this.increaseDay(level);
            this.updateTracker.removeIf(IDailyUpdate::inValid);
            this.updateTracker.forEach(update -> update.update(level));
        }
    }

    public void updateWeatherTo(ServerLevel level, EnumWeather weather) {
        EnumWeather nextWeather = null;
        float chance = level.random.nextFloat();
        EnumSeason season = this.currentSeason();
        if (canChangeToRuneyOrStormyWeather(level)) {
            float stormAdd = (season == EnumSeason.SUMMER || season == EnumSeason.WINTER) ? 0.04F : 0;
            if (chance < 0.03F)
                nextWeather = EnumWeather.RUNEY;
            else if (chance < 0.015F + stormAdd)
                nextWeather = EnumWeather.STORM;
        }
        if (nextWeather == null) {
            if (chance < 0.2F)
                nextWeather = EnumWeather.RAIN;
            else
                nextWeather = EnumWeather.CLEAR;
        }
        this.calendar.setWeather(level.getServer(), weather, nextWeather);
        this.setMCWeather(level);
        this.updateDelay = 100;
        this.setDirty();
    }

    private void setMCWeather(ServerLevel level) {
        switch (this.currentWeather()) {
            case RAIN -> level.setWeatherParameters(0, 24000, true, false);
            case CLEAR, RUNEY -> level.setWeatherParameters(24000, 0, false, false);
            case STORM -> level.setWeatherParameters(0, 24000, true, true);
        }
    }

    private boolean isCorrectWeather(ServerLevel level) {
        return switch (this.currentWeather()) {
            case RAIN -> level.isRaining();
            case CLEAR, RUNEY -> !level.isRaining() && !level.isThundering();
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
    }

    @Override
    public CompoundTag save(CompoundTag compoundNBT) {
        return this.calendar.write(compoundNBT);
    }
}
