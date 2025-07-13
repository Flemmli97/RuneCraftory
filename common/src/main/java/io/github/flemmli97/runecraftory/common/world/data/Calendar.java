package io.github.flemmli97.runecraftory.common.world.data;

import io.github.flemmli97.runecraftory.api.calendar.DayOfWeek;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.calendar.Weather;
import io.github.flemmli97.runecraftory.client.ClientCalendarHolder;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.integration.sereneseasons.SeasonsAccess;
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

public class Calendar {

    @Nullable
    private final RunecraftorySavedData handler;

    private Date current = new Date(1, 1, DayOfWeek.MONDAY, Season.SPRING);

    private Weather[] todaysForecast = new Weather[]{
            Weather.CLEAR
    };
    private Weather currentWeather = Weather.CLEAR;
    private Weather[] nextForecast = new Weather[]{
            Weather.CLEAR
    };
    private int updateDelay;

    public Calendar(@Nullable RunecraftorySavedData handler) {
        this.handler = handler;
    }

    public static Calendar get(Level level) {
        if (level.isClientSide()) {
            return ClientCalendarHolder.CLIENT_CALENDAR;
        }
        return RunecraftorySavedData.get(level.getServer()).getCalendar();
    }

    public static boolean canUpdateWeather(Level level) {
        return GeneralConfig.modifyWeather && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }

    public static boolean shouldUpdateWeather(Level level, Weather currentWeather) {
        if (currentWeather == Weather.RUNEY || currentWeather == Weather.STORM)
            return WorldUtils.dayTime(level) == 1;
        long time = WorldUtils.dayTime(level);
        return (time % 3000) == 1;
    }

    public Season currentSeason() {
        return this.date().season();
    }

    public Date date() {
        return this.current;
    }

    public void setDateDayAndSeason(MinecraftServer server, int dayOfYear, int date, DayOfWeek day, Season season) {
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
                DayOfWeek day = DayOfWeek.values()[Math.floorMod(this.current.day().ordinal() + diff, DayOfWeek.values().length)];
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
        DayOfWeek day = DayOfWeek.values()[Math.floorMod(date, DayOfWeek.values().length)];
        Season season = Season.values()[Math.floorMod(date / 30, Season.values().length)];
        this.setDateDayAndSeason(level.getServer(), (date % (30 * 4)) + 1, date % 30 + 1, day, season);
        LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), level.getServer());
        this.setDirty();
    }

    public void updateWeatherTo(ServerLevel level, Weather weather) {
        this.setWeather(level.getServer(), weather);
        this.setMCWeather(level);
        this.updateDelay = 100;
        this.setDirty();
    }

    private void createDailyWeather(ServerLevel level) {
        Weather[] nextWeather = new Weather[8];
        Season season = this.currentSeason();
        int rainCount = 0;
        for (int i = 0; i < nextWeather.length; i++) {
            float chance = level.random.nextFloat();
            if (i != 0) {
                if (nextWeather[0].wholeDay) {
                    nextWeather[i] = nextWeather[0];
                    return;
                }
            } else {
                float stormAdd = (season == Season.SUMMER || season == Season.WINTER) ? 0.04F : 0;
                if (chance < 0.03F)
                    nextWeather[i] = Weather.RUNEY;
                else if (chance < 0.015F + stormAdd)
                    nextWeather[i] = Weather.STORM;
                if (nextWeather[i] != null)
                    return;
            }
            float rainAdd = rainCount > 0 ? 0.5f - (rainCount - 1) * 0.2f : 0;
            if (i < 3)
                rainAdd += season == Season.SUMMER ? 0.1 : 0.05;
            if (chance < 0.1F + rainAdd) {
                nextWeather[i] = Weather.RAIN;
                rainCount++;
            } else
                nextWeather[i] = Weather.CLEAR;
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

    public Weather currentWeather() {
        return this.currentWeather;
    }

    public Weather getCurrentWeatherFor(ServerLevel level) {
        int i = (WorldUtils.dayTime(level) / 3000);
        if (i >= 0 && i < this.todaysForecast.length)
            return this.todaysForecast[i];
        return Weather.CLEAR;
    }

    public Weather[] todaysForecast() {
        return this.todaysForecast;
    }

    public Weather[] tomorrowsForecast() {
        return this.nextForecast;
    }

    public void setWeather(MinecraftServer server, Weather weather) {
        this.currentWeather = weather;
        LoaderNetwork.INSTANCE.sendToAll(new S2CCalendar(this), server);
    }

    public void updateWeathers(Weather[] nextDays) {
        this.todaysForecast = this.nextForecast;
        this.nextForecast = nextDays;
    }

    public void updateDirect(Calendar.Date date, Weather weather) {
        this.current = date;
        this.currentWeather = weather;
    }

    private void setDirty() {
        if (this.handler != null)
            this.handler.setDirty();
    }

    public void read(CompoundTag nbt) {
        this.current = new Date(nbt.getInt("DayOfYear"), nbt.getInt("Date"), DayOfWeek.valueOf(nbt.getString("Day")), Season.valueOf(nbt.getString("Season")));
        this.currentWeather = Weather.valueOf(nbt.getString("Weather"));
        ListTag list = nbt.getList("Forecast", Tag.TAG_STRING);
        this.todaysForecast = list.stream().map(t -> Weather.valueOf(t.getAsString()))
                .limit(8)
                .toArray(Weather[]::new);
        ListTag next = nbt.getList("NextForecast", Tag.TAG_STRING);
        this.nextForecast = next.stream().map(t -> Weather.valueOf(t.getAsString()))
                .limit(8)
                .toArray(Weather[]::new);
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

    public record Date(int dayOfYear, int date, DayOfWeek day, Season season) {

        public static final StreamCodec<ByteBuf, Date> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Date::dayOfYear,
                ByteBufCodecs.INT, Date::date,
                StreamCodecUtils.ofEnum(DayOfWeek.class), Date::day,
                StreamCodecUtils.ofEnum(Season.class), Date::season,
                Date::new);
    }
}
