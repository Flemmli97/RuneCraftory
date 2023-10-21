package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Arrays;

public class CalendarImpl {

    private int date = 1;
    private EnumDay day = EnumDay.MONDAY;
    private EnumSeason season = EnumSeason.SPRING;

    private EnumWeather[] todaysForecast = new EnumWeather[]{
            EnumWeather.CLEAR
    };
    private EnumWeather currentWeather = EnumWeather.CLEAR;
    private EnumWeather[] nextForecast = new EnumWeather[]{
            EnumWeather.CLEAR
    };

    public int date() {
        return this.date;
    }

    public EnumDay currentDay() {
        return this.day;
    }

    public EnumSeason currentSeason() {
        return this.season;
    }

    public void setDateDayAndSeason(int date, EnumDay day, EnumSeason season) {
        this.date = date;
        this.day = day;
        this.season = season;
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
        Platform.INSTANCE.sendToAll(new S2CCalendar(this), server);
    }

    public void updateWeathers(EnumWeather[] nextDays) {
        this.todaysForecast = this.nextForecast;
        this.nextForecast = nextDays;
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeInt(this.date);
        buffer.writeEnum(this.day);
        buffer.writeEnum(this.season);
        buffer.writeEnum(this.currentWeather);
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        this.date = buffer.readInt();
        this.day = buffer.readEnum(EnumDay.class);
        this.season = buffer.readEnum(EnumSeason.class);
        this.currentWeather = buffer.readEnum(EnumWeather.class);
    }

    public void read(CompoundTag nbt) {
        this.date = nbt.getInt("Date");
        this.day = EnumDay.valueOf(nbt.getString("Day"));
        this.season = EnumSeason.valueOf(nbt.getString("Season"));
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
        nbt.putInt("Date", this.date);
        nbt.putString("Day", this.day.toString());
        nbt.putString("Season", this.season.toString());
        nbt.putString("Weather", this.currentWeather.toString());
        ListTag list = new ListTag();
        Arrays.stream(this.todaysForecast).forEach(w -> list.add(StringTag.valueOf(w.toString())));
        nbt.put("Forecast", list);
        ListTag next = new ListTag();
        Arrays.stream(this.nextForecast).forEach(w -> next.add(StringTag.valueOf(w.toString())));
        nbt.put("NextForecast", next);
        return nbt;
    }
}
