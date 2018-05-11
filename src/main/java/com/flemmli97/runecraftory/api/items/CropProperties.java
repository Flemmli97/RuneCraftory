package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;

public class CropProperties {
	
    private EnumSeason bestSeason;
    private int growthDay, maxDrops;
    
    public CropProperties(EnumSeason season, int growth, int maxDrops)
    {
    	this.bestSeason=season;
    	this.growthDay=growth;
    	this.maxDrops=maxDrops;
    }
    
    public EnumSeason bestSeason()
    {
    	return this.bestSeason;
    }
    
    public int growth()
    {
    	return this.growthDay;
    }
    
    public int maxDrops()
    {
    	return this.maxDrops;
    }

}
