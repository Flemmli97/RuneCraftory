package com.flemmli97.runecraftory.common.datapack.manager;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class CropManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private Map<ResourceLocation, CropProperties> crops = ImmutableMap.of();

    public CropManager() {
        super(GSON, "crop_properties");
    }

    public CropProperties get(Item item) {
        return crops.get(item.getRegistryName());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, CropProperties> builder = ImmutableMap.builder();
        data.forEach((res, el) -> {
            try {
                builder.put(res, GSON.fromJson(el, CropProperties.class));
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse crop properties json {}", res);
                ex.fillInStackTrace();
            }
        });
        this.crops = builder.build();
    }
}
