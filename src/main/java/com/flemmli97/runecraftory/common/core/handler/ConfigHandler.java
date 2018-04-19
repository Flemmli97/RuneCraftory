package com.flemmli97.runecraftory.common.core.handler;

import java.io.File;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	
	//Crops
	
	//Equipment + Player
	public static float xpMultiplier;
	public static float skillXpMultiplier;
	
	//Mobs
	public static float tamingMultiplier;
	public static float levelIncreaseMultiplier;
	public static float dropRateMultiplier;
	
	//Generation
	public static int amrosiaRarity;
	public static int mineralRarity;

	public static void initConfig(FMLPreInitializationEvent e)
	{
		mainConfig(new Configuration(new File(e.getModConfigurationDirectory()+"/" + LibReference.MODID + "/", "main.cfg")));
	}
	private static void mainConfig(Configuration config) {
		config.load();
		
		config.save();
	}
	
	private static float getFloatConfig(Configuration config, String name, String category, float defaultValue, String comment)
	{
		Property prop = config.get(category, name, Float.toString(defaultValue), name);
        prop.setLanguageKey(name);
        prop.setComment(comment + "[default: " + defaultValue + "]");
        try
        {
            float parseFloat = Float.parseFloat(prop.getString());
            return Math.max(0, parseFloat);
        }
        catch (Exception e)
        {
            FMLLog.log.error("Failed to get float for {}/{}", name, category, e);
        }
        return defaultValue;
	}
}