package com.flemmli97.runecraftory.api.items;

import java.util.EnumSet;
import java.util.Set;

import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;

public class CropProperties
{
    private EnumSet<CalendarHandler.EnumSeason> bestSeason = EnumSet.noneOf(CalendarHandler.EnumSeason.class);
    private EnumSet<CalendarHandler.EnumSeason> badSeason = EnumSet.noneOf(CalendarHandler.EnumSeason.class);

    private int growth;
    private int maxDrops;
    private boolean regrowable;
    
    public CropProperties(EnumSet<CalendarHandler.EnumSeason> season, EnumSet<CalendarHandler.EnumSeason> badseason, int growth, int maxDrops, boolean regrowable) 
    {
        this.bestSeason = season;
        this.growth = growth;
        this.maxDrops = maxDrops;
        this.regrowable = regrowable;
    }
    
    public Set<CalendarHandler.EnumSeason> bestSeasons() {
        return this.bestSeason;
    }
    
    public Set<CalendarHandler.EnumSeason> badSeasons() {
        return this.badSeason;
    }
    
    public int growth() {
        return this.growth;
    }
    
    public int maxDrops() {
        return this.maxDrops;
    }
    
    public boolean regrowable() {
        return this.regrowable;
    }
    
    public float seasonMultiplier(EnumSeason season)
    {
    	if(this.bestSeason.contains(season))
	    		return 1.5f;
    	if(this.badSeason.contains(season))
	    		return 2/3f;
    	return 1;
    }
    
    @Override
    public String toString() {
        return "[BestSeasons:" + this.bestSeason+";BadSeasons:"+ this.badSeason + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
    }
}
