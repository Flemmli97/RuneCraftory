package com.flemmli97.runecraftory.common.core.handler.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Configuration;

public class CropConfig
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    protected static void init(Configuration config) {
        config.load();
        cropData(config);
        config.save();
    }
    
    protected static void jsonConfig(File file) 
    {
        try 
        {
            JsonArray json = JsonUtils.fromJson(CropConfig.GSON, new FileReader(file), JsonArray.class);
            if (json != null && !json.isJsonNull()) 
            {
                for (JsonElement element : json) 
                {
                    if (element instanceof JsonObject) 
                    {
                        JsonObject obj = (JsonObject)element;
                        CropProperties fromConfig = new CropProperties(
                        		EnumSeason.fromString(JsonUtils.getString(obj, "season")), 
                        		JsonUtils.getInt(obj, "growth"), JsonUtils.getInt(obj, "drops"), 
                        		JsonUtils.getBoolean(obj, "regrowable"));
                        String crop = JsonUtils.getString(obj, "crop");
                        CropMap.putProps(crop, fromConfig);
                    }
                }
            }
        }
        catch (FileNotFoundException e2) 
        {
            LibReference.logger.debug("No json file for crops, creating an empty one now");
            try 
            {
                FileWriter writer = new FileWriter(file);
                JsonWriter json2 = CropConfig.GSON.newJsonWriter((Writer)writer);
                json2.beginArray();
                //TODO default vanilla props
                json2.endArray();
                writer.close();
            }
            catch (IOException e1) 
            {
                LibReference.logger.error("Error creating json file for crops");
                e1.printStackTrace();
            }
        }
    }
    
    private static void cropData(Configuration config) {
        for (String id : CropMap.allOreDictCrops()) {
            CropProperties prop = CropMap.getPropertiesFromID(id);
            if (prop != null) {
                CropProperties fromConfig = new CropProperties(CalendarHandler.EnumSeason.fromString(config.getString("Season", id, prop.bestSeason().formattingText(), "The Season where this crop grows best")), ConfigHandler.getIntConfig(config, "Growth", id, prop.growth(), "Time in days it take for this crop to grow"), ConfigHandler.getIntConfig(config, "Drops", id, prop.maxDrops(), "Amount of drops per harvest"), config.getBoolean("Regrowable", id, prop.regrowable(), "If this crop can regrow"));
                CropMap.putProps(id, fromConfig);
            }
        }
    }
}
