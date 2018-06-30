package com.flemmli97.runecraftory.common.core.handler.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import net.minecraft.util.JsonUtils;

public class ItemStatsConfig
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();;
    
    protected static void jsonConfig(File file) 
    {
        try 
        {
            JsonArray json = JsonUtils.fromJson(ItemStatsConfig.GSON, new FileReader(file), JsonArray.class);
            if (json != null && !json.isJsonNull()) 
            {
                for (JsonElement jsonElement : json) 
                {
                    if (jsonElement instanceof JsonObject) 
                    {
                        JsonObject obj = (JsonObject)jsonElement;
                        String item = JsonUtils.getString(obj, "item");
                        int meta = obj.has("meta") ? JsonUtils.getInt(obj.get("meta"), "meta") : -1;
                        int buy = JsonUtils.getInt(obj, "buy");
                        int sell = JsonUtils.getInt(obj, "sell");
                        int diff = JsonUtils.getInt(obj, "difficulty");
                        EnumElement element = EnumElement.NONE;
                        if (obj.has("element")) 
                        {
                            element = EnumElement.fromName(JsonUtils.getString(obj, "element"));
                        }
                        Map<ItemStatAttributes, Integer> map = Maps.newHashMap();
                        for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(obj, "stats").entrySet()) 
                        {
                            ItemStatAttributes att = ItemStatAttributes.ATTRIBUTESTRINGMAP.get(entry.getKey());
                            if (att != null) 
                            {
                                map.put(att, entry.getValue().getAsInt());
                            }
                            else 
                            {
                                LibReference.logger.error("Attribute with name " + entry.getKey() + " does not exist");
                            }
                        }
                        ItemStat stat = new ItemStat(buy, sell, diff, element, map);
                        boolean isRegName = item.contains(":");
                        if (isRegName) 
                        {
                            ItemStatMap.preAdd(new ItemProperties(item, (meta == -1) ? 32767 : meta), stat);
                        }
                        else 
                        {
                            ItemStatMap.preAdd(item, stat);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e2) 
        {
            LibReference.logger.debug("No json file for prices, creating an empty one now");
            try 
            {
                FileWriter writer = new FileWriter(file);
                JsonWriter json = ItemStatsConfig.GSON.newJsonWriter((Writer)writer);
                json.beginArray();
                json.endArray();
                writer.close();
            }
            catch (IOException e1) 
            {
                LibReference.logger.error("Error creating json file for prices");
                e1.printStackTrace();
            }
        }
    }
}
