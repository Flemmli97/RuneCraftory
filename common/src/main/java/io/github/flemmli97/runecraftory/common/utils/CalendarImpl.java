package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.client.ClientCalendarHolder;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.world.RunecraftorySavedData;
import io.github.flemmli97.runecraftory.integration.seasons.SeasonsAccess;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class CalendarImpl {

    @Nullable
    private final RunecraftorySavedData handler;

    private Date current = new Date(1, 1, EnumDay.MONDAY, EnumSeason.SPRING);

    private EnumWeather[] todaysForecast = new EnumWeather[]{
            EnumWeather.CLEAR
    };
    private EnumWeather currentWeather = EnumWeather.CLEAR;
    private EnumWeather[] nextForecast = new EnumWeather[]{
            EnumWeather.CLEAR
    };
    private int updateDelay;

    public CalendarImpl(@Nullable RunecraftorySavedData handler) {
        this.handler = handler;
    }

    public static CalendarImpl get(Level level) {
        if (level.isClientSide()) {
            return ClientCalendarHolder.CLIENT_CALENDAR;
        }
        return RunecraftorySavedData.get(level.getServer()).getCalendar();
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

    public EnumSeason currentSeason() {
        return this.date().season();
    }

    public Date date() {
        return this.current;
    }

    public void setDateDayAndSeason(MinecraftServer server, int dayOfYear, int date, EnumDay day, EnumSeason season) {
        this.current = new Date(dayOfYear, date, day, season);
        LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), server);
        this.setDirty();
    }

    public void tick(ServerLevel level, boolean isUpdateTime) {
        SeasonsAccess.SeasonData seasons = SeasonsAccess.getDate(level);
        if (seasons != null) {
            // Sync seasons from serene seasons
            boolean changed = this.current.dayOfYear() != seasons.dayOfYear() || this.current.season() != seasons.season();
            if (changed) {
                int diff = seasons.dayOfYear() - this.current.dayOfYear();
                while (diff < 0)
                    diff += seasons.daysPerYear();
                EnumDay day = EnumDay.values()[Math.floorMod(this.current.day().ordinal() + diff, EnumDay.values().length)];
                this.current = new Date(seasons.dayOfYear(), seasons.dayOfSeason(), day, seasons.season());
                LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), level.getServer());
                this.setDirty();
            }
        } else if (isUpdateTime) {
            this.increaseDay(level);
            this.createDailyWeather(level);
        }

        boolean doWeather = canUpdateWeather(level);
        if (doWeather && (isUpdateTime || shouldUpdateWeather(level, this.currentWeather()))) {
            this.updateWeatherTo(level, this.getCurrentWeatherFor(level));
        }
        // Checks if current weather is correct and if not corrects it
//        if (--this.updateDelay <= 0) {
//            if (doWeather && !this.isCorrectWeather(level))
//                this.setMCWeather(level);
//            this.updateDelay = 40;
//        }
    }

    private void increaseDay(ServerLevel level) {
        int date = WorldUtils.day(level);
        EnumDay day = EnumDay.values()[Math.floorMod(date, EnumDay.values().length)];
        EnumSeason season = EnumSeason.values()[Math.floorMod(date / 30, EnumSeason.values().length)];
        this.setDateDayAndSeason(level.getServer(), (date % (30 * 4)) + 1, date % 30 + 1, day, season);
        LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), level.getServer());
        this.setDirty();
    }

    public void updateWeatherTo(ServerLevel level, EnumWeather weather) {
        this.setWeather(level.getServer(), weather);
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
        this.updateWeathers(nextWeather);
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

    public EnumWeather currentWeather() {
        return this.currentWeather;
    }

    public EnumWeather getCurrentWeatherFor(ServerLevel level) {
        int i = (WorldUtils.dayTime(level) / 3000);
        if (i >= 0 && i < this.todaysForecast.length)
            return this.todaysForecast[i];
        return EnumWeather.CLEAR;
    }

    public EnumWeather[] todaysForecast() {
        return this.todaysForecast;
    }

    public EnumWeather[] tomorrowsForecast() {
        return this.nextForecast;
    }

    public void setWeather(MinecraftServer server, EnumWeather weather) {
        this.currentWeather = weather;
        LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), server);
    }

    public void updateWeathers(EnumWeather[] nextDays) {
        this.todaysForecast = this.nextForecast;
        this.nextForecast = nextDays;
    }

    public void updateDirect(CalendarImpl.Date date, EnumWeather weather) {
        this.current = date;
        this.currentWeather = weather;
    }

    private void setDirty() {
        if (this.handler != null)
            this.handler.setDirty();
    }

    public void read(CompoundTag nbt) {
        this.current = new Date(nbt.getInt("DayOfYear"), nbt.getInt("Date"), EnumDay.valueOf(nbt.getString("Day")), EnumSeason.valueOf(nbt.getString("Season")));
        this.currentWeather = EnumWeather.valueOf(nbt.getString("Weather"));
        ListTag list = nbt.getList("Forecast", Tag.TAG_STRING);
        this.todaysForecast = list.stream().map(t -> EnumWeather.valueOf(t.getAsString()))
                .limit(8)
                .toArray(EnumWeather[]::new);
        ListTag next = nbt.getList("NextForecast", Tag.TAG_STRING);
        this.nextForecast = next.stream().map(t -> EnumWeather.valueOf(t.getAsString()))
                .limit(8)
                .toArray(EnumWeather[]::new);
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("DayOfYear", this.current.dayOfYear());
        nbt.putInt("Date", this.current.date());
        nbt.putString("Day", this.current.day().toString());
        nbt.putString("Season", this.current.season().toString());
        nbt.putString("Weather", this.currentWeather.toString());
        ListTag list = new ListTag();
        Arrays.stream(this.todaysForecast).forEach(w -> list.add(StringTag.valueOf(w.toString())));
        nbt.put("Forecast", list);
        ListTag next = new ListTag();
        Arrays.stream(this.nextForecast).forEach(w -> next.add(StringTag.valueOf(w.toString())));
        nbt.put("NextForecast", next);
        return nbt;
    }

    public record Date(int dayOfYear, int date, EnumDay day, EnumSeason season) {

        public static final StreamCodec<ByteBuf, Date> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Date::dayOfYear,
                ByteBufCodecs.INT, Date::date,
                StreamCodecUtils.ofEnum(EnumDay.class), Date::day,
                StreamCodecUtils.ofEnum(EnumSeason.class), Date::season,
                Date::new);
        
    }
}
