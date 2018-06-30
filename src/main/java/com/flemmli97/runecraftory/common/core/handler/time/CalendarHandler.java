package com.flemmli97.runecraftory.common.core.handler.time;

import com.flemmli97.runecraftory.common.network.PacketCalendar;
import com.flemmli97.runecraftory.common.network.PacketHandler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class CalendarHandler extends WorldSavedData{

	private static final String id = "RCCalendar";

	private EnumSeason season=EnumSeason.SPRING;
	private EnumDay day = EnumDay.MONDAY;
	private int date = 1;
	
	public CalendarHandler(String id) {
		super(id);
	}
	
	public CalendarHandler() {
		this(id);
	}
	
	public static CalendarHandler get(World world)
	{
		MapStorage storage = world.getMapStorage();
		CalendarHandler data = (CalendarHandler)storage.getOrLoadData(CalendarHandler.class, id);
		if (data == null)
		{
			data = new CalendarHandler();
			storage.setData(id, data);
		}
		return data;
	}
	
	public void setDateDayAndSeason(int date, EnumDay day, EnumSeason season, boolean sideServer)
	{
		this.date=date;
		this.day=day;
		this.season=season;
		if(sideServer)
			PacketHandler.sendToAll(new PacketCalendar(this));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.date=nbt.getInteger("date");
		this.season=EnumSeason.valueOf(nbt.getString("season"));
		this.day=EnumDay.valueOf(nbt.getString("day"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("date", this.date);	
		compound.setString("season", this.season.toString());
		compound.setString("day", this.day.toString());
		return compound;
	}
	
	public void increaseDay()
	{
		this.date++;
		this.day=EnumDay.nextDay(this.day);
		if(this.date>30)
		{
			this.season=EnumSeason.nextSeason(this.season);
			this.date=1;
		}
		PacketHandler.sendToAll(new PacketCalendar(this));
		this.markDirty();
	}
	
	public EnumSeason currentSeason()
	{
		return this.season;
	}
	
	public int date()
	{
		return this.date;
	}
	
	public EnumDay currentDay()
	{
		return this.day;
	}
	
	public static enum EnumSeason
	{
		SPRING("red", "Spring"),
		SUMMER("dark_green", "Summer"),
		FALL("gold", "Fall"),
		WINTER("white", "Winter");
		private String color, id;
		EnumSeason(String color, String id)
		{
			this.color=color;
			this.id=id;
		}
		public static EnumSeason nextSeason(EnumSeason season)
		{
			switch(season)
			{
			case FALL: return WINTER;
			case SPRING: return SUMMER;
			case SUMMER: return FALL;
			case WINTER: return SPRING;
			default: return SPRING;
			}
		}
		
		public String getColor()
		{
			return this.color;
		}
		
		public String formattingText()
		{
			return this.id;
		}
		
		public static EnumSeason fromString(String name)
		{
			for(EnumSeason season : EnumSeason.values())
				if(season.formattingText().equals(name))
					return season;
			return EnumSeason.SPRING;
		}
	}
	
	public static enum EnumDay
	{
		MONDAY("Mon"),
		TUESDAY("Tue"),
		WEDNESDAY("Wed"),
		THURSDAY("Thur"),
		FRIDAY("Fri"),
		SATURDAY("Sat"),
		SUNDAY("Sun");
		
		String id;
		EnumDay(String id)
		{
			this.id=id;
		}
		public static EnumDay nextDay(EnumDay day)
		{
			switch(day)
			{
				case FRIDAY: return SATURDAY;
				case MONDAY: return TUESDAY;
				case SATURDAY: return SUNDAY;
				case SUNDAY: return MONDAY;
				case THURSDAY: return FRIDAY;
				case TUESDAY: return WEDNESDAY;
				case WEDNESDAY: return THURSDAY;
				default: return MONDAY;
			}
		}
		
		public String text()
		{
			return this.id;
		}
	}
}
