package com.flemmli97.runecraftory.api.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class ItemStatProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(Attribute.class, new AttributeSerializer()).create();

    private final Map<ResourceLocation, ItemStat.MutableItemStat> data = new HashMap<>();
    private final DataGenerator gen;

    public ItemStatProvider(DataGenerator gen) {
        this.gen = gen;
    }

    protected abstract void addStats();

    @Override
    public void act(DirectoryCache cache) {
        this.addStats();
        data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/item_stats/" + res.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, GSON.toJsonTree(builder), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "ItemStats";
    }

    public void addStat(Item item, int buy, int sell, int upgrade) {
        this.data.put(item.getRegistryName(), new ItemStat.MutableItemStat(buy, sell, upgrade));
    }

    public void addStat(Item item, ItemStat.MutableItemStat builder) {
        this.data.put(item.getRegistryName(), builder);
    }
}
