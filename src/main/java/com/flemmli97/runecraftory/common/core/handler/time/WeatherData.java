package com.flemmli97.runecraftory.common.core.handler.time;

import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;

public class WeatherData extends WorldSavedData{

	private static final String id = "RCWeather";
	
	private EnumWeather nextWeather = EnumWeather.CLEAR;
	private EnumWeather currentWeather = EnumWeather.CLEAR;

	public WeatherData(String id) {
		super(id);
	}
	
	public WeatherData() {
		this(id);
	}
	
	public static WeatherData get(World world)
	{
		MapStorage storage = world.getMapStorage();
		WeatherData data = (WeatherData)storage.getOrLoadData(WeatherData.class, id);
		if (data == null)
		{
			data = new WeatherData();
			storage.setData(id, data);
		}
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.currentWeather=EnumWeather.values()[nbt.getInteger("CurrentWeather")];
		this.nextWeather=EnumWeather.values()[nbt.getInteger("NextWeather")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("CurrentWeather", this.currentWeather.ordinal());
		compound.setInteger("NextWeather", this.nextWeather.ordinal());
		return compound;
	}
	
	public void update(World world)
	{
		if(this.canUpdateWeather(world))
		{
			this.currentWeather=this.nextWeather;
			float chance = world.rand.nextFloat();
			EnumSeason season = CalendarHandler.get(world).currentSeason();
			float stormAdd = (season==EnumSeason.SUMMER||season==EnumSeason.WINTER)?0.1F:0;
			if(chance<0.01F)
				this.nextWeather=EnumWeather.RUNEY;
			else if(chance<0.05F+stormAdd)
				this.nextWeather=EnumWeather.STORM;
			else if(chance<0.4F)
				this.nextWeather=EnumWeather.RAIN;
			else
				this.nextWeather=EnumWeather.CLEAR;
		}
		WorldInfo worldinfo = world.getWorldInfo();
		switch(this.currentWeather)
		{
			case CLEAR:
				worldinfo.setCleanWeatherTime(24000);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
				break;
			case RAIN:
				worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(24000);
                worldinfo.setThunderTime(24000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
				break;
			case RUNEY:
				worldinfo.setCleanWeatherTime(24000);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
				break;
			case STORM:
				worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(24000);
                worldinfo.setThunderTime(24000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
				break;
		}

	}
	
	public EnumWeather currentWeather()
	{
		return this.currentWeather;
	}
	
	public EnumWeather nextWeather()
	{
		return this.nextWeather;
	}
	
	private boolean canUpdateWeather(World world)
	{
		if(world.getGameRules().getBoolean("doDaylightCycle"))
		{
			if(this.currentWeather==EnumWeather.RUNEY || this.currentWeather == EnumWeather.STORM)
				return world.getWorldTime() % 24000 == 1;
			long mod =  world.getWorldTime() % 24000;
			return mod == 1 || mod == 6001 || mod == 12001 || mod == 18001;
		}
		return false;
	}
	
	public static enum EnumWeather {

		CLEAR,
		RAIN,
		STORM,
		RUNEY;
	}
}
