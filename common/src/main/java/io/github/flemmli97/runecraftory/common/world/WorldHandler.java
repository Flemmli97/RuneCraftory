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

    public WorldHandler() {
    }

    private WorldHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static WorldHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(WorldHandler::new, WorldHandler::new, id);
    }

    public static boolean canUpdateWeather(Level world, EnumWeather currentWeather) {
        if (GeneralConfig.modifyWeather && world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            if (currentWeather == EnumWeather.RUNEY || currentWeather == EnumWeather.STORM)
                return WorldUtils.dayTime(world) == 1;
            long time = WorldUtils.dayTime(world);
            return time == 1 || time == 6001 || time == 12001 || time == 18001;
        }
        return false;
    }

    public CalendarImpl getCalendar() {
        return this.calendar;
    }

    public void setDateDayAndSeason(MinecraftServer server, int date, EnumDay day, EnumSeason season) {
        this.calendar.setDateDayAndSeason(date, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), server);
        this.setDirty();
    }

    public void increaseDay(ServerLevel world) {
        int date = WorldUtils.day(world);
        EnumDay day = EnumDay.values()[Math.floorMod(date, EnumDay.values().length)];
        EnumSeason season = EnumSeason.values()[Math.floorMod(date / 30, EnumSeason.values().length)];
        this.calendar.setDateDayAndSeason(date % 30 + 1, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), world.getServer());
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

    public void update(ServerLevel world) {
        if (canUpdateWeather(world, this.currentWeather())) {
            this.updateWeatherTo(world, this.nextWeather());
        }
        if (WorldUtils.canUpdateDaily(world)) {
            this.increaseDay(world);
            this.updateTracker.removeIf(IDailyUpdate::inValid);
            this.updateTracker.forEach(update -> update.update(world));
        }
    }

    public void updateWeatherTo(ServerLevel world, EnumWeather weather) {
        EnumWeather nextWeather;
        float chance = world.random.nextFloat();
        EnumSeason season = this.currentSeason();
        float stormAdd = (season == EnumSeason.SUMMER || season == EnumSeason.WINTER) ? 0.1F : 0;
        if (chance < 0.01F)
            nextWeather = EnumWeather.RUNEY;
        else if (chance < 0.05F + stormAdd)
            nextWeather = EnumWeather.STORM;
        else if (chance < 0.4F)
            nextWeather = EnumWeather.RAIN;
        else
            nextWeather = EnumWeather.CLEAR;
        this.calendar.setWeather(world.getServer(), weather, nextWeather);
        switch (this.currentWeather()) {
            case RAIN -> world.setWeatherParameters(0, 24000, true, false);
            case CLEAR, RUNEY -> world.setWeatherParameters(24000, 0, false, false);
            case STORM -> world.setWeatherParameters(0, 24000, true, true);
        }
        this.setDirty();
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
