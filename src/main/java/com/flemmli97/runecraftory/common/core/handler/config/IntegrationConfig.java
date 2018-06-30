package com.flemmli97.runecraftory.common.core.handler.config;

import net.minecraftforge.common.config.Configuration;

public class IntegrationConfig
{
    public static boolean waila = true;
    public static boolean jei = true;
    public static boolean harvestCraft = true;
    public static boolean season = true;
    
    protected static void init(final Configuration config) {
        config.load();
        waila = config.getBoolean("Waila", "integration", IntegrationConfig.waila, "Waila integration");
        //jei = config.getBoolean("JEI", "", jei, "JEI integration");
  		//harvestCraft = config.getBoolean("HarvestCraft", "", harvestCraft, "HarvestCraft integration");
  		//season = config.getBoolean("SereneSeason", "", season, "SereneSeason integration"); 
        config.save();
    }
}
