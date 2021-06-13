package io.github.flemmli97.runecraftory.common.world;

import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Set;

public class WorldHandler extends WorldSavedData {

    private static final String id = "RCCalendar";

    private final CalendarImpl calendar = new CalendarImpl();

    /**
     * Non persistent tracker to update stuff daily
     */
    private final Set<IDailyUpdate> updateTracker = Sets.newConcurrentHashSet();

    public WorldHandler() {
        this(id);
    }

    private WorldHandler(String identifier) {
        super(identifier);
    }

    public static WorldHandler get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData().getOrCreate(WorldHandler::new, id);
    }

    public CalendarImpl getCalendar() {
        return this.calendar;
    }

    public void setDateDayAndSeason(int date, EnumDay day, EnumSeason season) {
        this.calendar.setDateDayAndSeason(date, day, season);
        PacketHandler.sendToAll(new S2CCalendar(this.calendar));
        this.markDirty();
    }

    public void increaseDay(World world) {
        int date = WorldUtils.day(world);
        EnumDay day = EnumDay.values()[Math.floorMod(date, EnumDay.values().length)];
        EnumSeason season = EnumSeason.values()[Math.floorMod(date / 30, EnumSeason.values().length)];
        this.calendar.setDateDayAndSeason(date % 30 + 1, day, season);
        PacketHandler.sendToAll(new S2CCalendar(this.calendar));
        this.markDirty();
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

    public void update(ServerWorld world) {
        if (WorldUtils.canUpdateDaily(world)) {
            this.increaseDay(world);
            this.updateTracker.removeIf(IDailyUpdate::inValid);
            this.updateTracker.forEach(update -> update.update(world));
        }
        if (canUpdateWeather(world, this.currentWeather())) {
            EnumWeather current = this.nextWeather();
            EnumWeather nextWeather;
            float chance = world.rand.nextFloat();
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
            this.calendar.setWeather(current, nextWeather);
            switch (this.currentWeather()) {
                case RAIN:
                    world.setWeather(0, 24000, true, false);
                    break;
                case CLEAR:
                case RUNEY:
                    world.setWeather(24000, 0, false, false);
                    break;
                case STORM:
                    world.setWeather(0, 24000, true, true);
                    break;
            }
            this.markDirty();
        }
    }

    public void addToTracker(IDailyUpdate update) {
        this.updateTracker.add(update);
    }

    public boolean removeFromTracker(IDailyUpdate update) {
        return this.updateTracker.remove(update);
    }

    @Override
    public void read(CompoundNBT compoundNBT) {
        this.calendar.read(compoundNBT);
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        return this.calendar.write(compoundNBT);
    }

    public static boolean canUpdateWeather(World world, EnumWeather currentWeather) {
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            if (currentWeather == EnumWeather.RUNEY || currentWeather == EnumWeather.STORM)
                return WorldUtils.dayTime(world) == 1;
            long time = WorldUtils.dayTime(world);
            return time == 1 || time == 6001 || time == 12001 || time == 18001;
        }
        return false;
    }
}
