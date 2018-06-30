package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;

public class CropProperties
{
    private CalendarHandler.EnumSeason bestSeason;
    private int growthDay;
    private int maxDrops;
    private boolean regrowable;
    
    public CropProperties(CalendarHandler.EnumSeason season, int growth, int maxDrops, boolean regrowable) 
    {
        this.bestSeason = season;
        this.growthDay = growth;
        this.maxDrops = maxDrops;
        this.regrowable = regrowable;
    }
    
    public CalendarHandler.EnumSeason bestSeason() {
        return this.bestSeason;
    }
    
    public int growth() {
        return this.growthDay;
    }
    
    public int maxDrops() {
        return this.maxDrops;
    }
    
    public boolean regrowable() {
        return this.regrowable;
    }
    
    @Override
    public String toString() {
        return "[Season:" + this.bestSeason + ";Growth:" + this.growthDay + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
    }
}
