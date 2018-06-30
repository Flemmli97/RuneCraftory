package com.flemmli97.runecraftory.common.core.handler.config;

import com.flemmli97.runecraftory.common.lib.*;
import java.io.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.common.config.*;

public class ConfigHandler
{
	//General
	public static boolean debugMode;
	public static boolean mobs;
	public static boolean crops;
	public static boolean rp;
	
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
    
    public static void initConfigMain() {
        File configDir = new File(LibReference.modDir + "/" + LibReference.MODID + "/");
        mainConfig(new Configuration(new File(configDir, "main.cfg")));
        IntegrationConfig.init(new Configuration(new File(configDir, "compat.cfg")));
        MobConfig.init(new Configuration(new File(configDir, "mobs.cfg")));
        CropConfig.init(new Configuration(new File(configDir, "crops.cfg")));
        SpawnConfig.init(new Configuration(new File(configDir, "spawn.cfg")));
        GenerationConfig.init(new Configuration(new File(configDir, "gen.cfg")));
        File customizationDir = new File(LibReference.modDir + "/" + "runecraftory" + "/customization/");
        if (!customizationDir.exists()) {
            customizationDir.mkdir();
        }
        CropConfig.jsonConfig(new File(customizationDir, "cropintegration.json"));
        ItemStatsConfig.jsonConfig(new File(customizationDir, "stats.json"));
    }
    
    private static void mainConfig(Configuration config) {
        config.load();
        debugMode = config.getBoolean("Debug Mode", "general", false, "");
        mobs = config.getBoolean("Enable entity module", "general", true, "Enables/Disable entities in general from this mod. You will not be able to get drops normally if you disable this");
        config.save();
    }
    
    protected static float getFloatConfig(Configuration config, String name, String category, float defaultValue, String comment) {
        Property prop = config.get(category, name, Float.toString(defaultValue), name);
        prop.setLanguageKey(name);
        prop.setComment(comment + "[default: " + defaultValue + "]");
        try {
            float parseFloat = Float.parseFloat(prop.getString());
            return Math.max(0.0f, parseFloat);
        }
        catch (Exception e) {
            FMLLog.log.error("Failed to get float for {}/{}", (Object)name, (Object)category, (Object)e);
            return defaultValue;
        }
    }
    
    protected static int getIntConfig(Configuration config, String name, String category, int defaultValue, String comment) {
        Property prop = config.get(category, name, Integer.toString(defaultValue), name);
        prop.setLanguageKey(name);
        prop.setComment(comment + "[default: " + defaultValue + "]");
        try {
            int parseInt = Integer.parseInt(prop.getString());
            return Math.max(0, parseInt);
        }
        catch (Exception e) {
            FMLLog.log.error("Failed to get float for {}/{}", (Object)name, (Object)category, (Object)e);
            return defaultValue;
        }
    }
}
