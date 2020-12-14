package com.flemmli97.runecraftory.common.datapack.manager;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.AttributeSerializer;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ItemStatManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Attribute.class, new AttributeSerializer()).create();
    private Map<ResourceLocation, ItemStat> itemstat = ImmutableMap.of();

    public ItemStatManager() {
        super(GSON, "item_stats");
    }

    public ItemStat get(Item item) {
        return itemstat.get(item.getRegistryName());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, ItemStat> builder = ImmutableMap.builder();
        data.forEach((res, el) -> {
            try {
                builder.put(res, GSON.fromJson(el, ItemStat.class));
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse item stat json {}", res);
                ex.fillInStackTrace();
            }
        });
        this.itemstat = builder.build();
    }
}
