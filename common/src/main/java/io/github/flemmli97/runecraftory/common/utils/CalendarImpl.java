package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.network.S2CRuneyWeatherData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

public class CalendarImpl {

    private int date = 1;
    private EnumDay day = EnumDay.MONDAY;
    private EnumSeason season = EnumSeason.SPRING;
    private EnumWeather nextWeather = EnumWeather.CLEAR;
    private EnumWeather currentWeather = EnumWeather.CLEAR;

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

    public EnumWeather nextWeather() {
        return this.nextWeather;
    }

    public void setWeather(MinecraftServer server, EnumWeather weather, EnumWeather nextWeather) {
        this.currentWeather = weather;
        this.nextWeather = nextWeather;
        Platform.INSTANCE.sendToAll(new S2CRuneyWeatherData(this.currentWeather == EnumWeather.RUNEY), server);
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeInt(this.date);
        buffer.writeEnum(this.day);
        buffer.writeEnum(this.season);
        buffer.writeEnum(this.currentWeather);
        buffer.writeEnum(this.nextWeather);
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        this.date = buffer.readInt();
        this.day = buffer.readEnum(EnumDay.class);
        this.season = buffer.readEnum(EnumSeason.class);
        this.currentWeather = buffer.readEnum(EnumWeather.class);
        this.nextWeather = buffer.readEnum(EnumWeather.class);
    }

    public void read(CompoundTag nbt) {
        this.date = nbt.getInt("Date");
        this.day = EnumDay.valueOf(nbt.getString("Day"));
        this.season = EnumSeason.valueOf(nbt.getString("Season"));
        this.currentWeather = EnumWeather.valueOf(nbt.getString("Weather"));
        this.nextWeather = EnumWeather.valueOf(nbt.getString("NextWeather"));
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("Date", this.date);
        nbt.putString("Day", this.day.toString());
        nbt.putString("Season", this.season.toString());
        nbt.putString("Weather", this.currentWeather.toString());
        nbt.putString("NextWeather", this.nextWeather.toString());
        return nbt;
    }
}
