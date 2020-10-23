package com.flemmli97.runecraftory.mobs.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.utils.ConfigUtils;
import com.flemmli97.tenshilib.common.config.JsonConfig;
import com.flemmli97.tenshilib.common.utils.ResourceStream;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SpawnConfig {

    private final JsonConfig<JsonObject> conf;

    private Map<String, List<String>> rawEntityBiomeMap = Maps.newHashMap();
    private Map<String, List<String>> rawEntityBiomeTypeMap = Maps.newHashMap();

    public SpawnConfig(Path file) {
        File def = file.resolve("default").resolve("DefaultSpawns.json").toFile();
        ConfigUtils.copyDefaultConfigs(def, "DefaultSpawns.json");
        this.conf = new JsonConfig<>(file.resolve("mob_spawns.json").toFile(), JsonObject.class, def);
        RuneCraftory.logger.info("Reading gate spawns");
        JsonObject obj = this.conf.getElement();
        obj.entrySet().forEach(e -> {
            if(e.getValue().isJsonObject()){
                JsonObject o = (JsonObject) e.getValue();
                JsonElement el;
                int weight = o.has("weight")?o.get("weight").getAsInt():0;
                int dist = o.has("minSpawnDist")?o.get("minSpawnDist").getAsInt():0;
                String raw = e.getKey() + "," + dist + "," + weight;
                if(o.has("biomes") && (el = o.get("biomes")).isJsonArray()){
                    ((JsonArray)el).forEach(ae-> rawEntityBiomeMap.merge(ae.getAsString(), Lists.newArrayList(raw), (old, v)->{
                        old.add(raw);
                        return old;
                    }));
                }
                if(o.has("biomeTypes") && (el = o.get("biomeTypes")).isJsonArray()){
                    ((JsonArray)el).forEach(ae-> rawEntityBiomeTypeMap.merge(ae.getAsString(), Lists.newArrayList(raw), (old, v)->{
                        old.add(raw);
                        return old;
                    }));
                }
            }
        });
    }

    public List<String> getEntityFromBiome(ResourceLocation loc){
        return rawEntityBiomeMap.getOrDefault(loc.toString(), Lists.newArrayList());
    }

    public List<String> getEntityFromBiomeType(BiomeDictionary.Type type){
        return rawEntityBiomeTypeMap.getOrDefault(type.getName(), Lists.newArrayList());
    }

}
