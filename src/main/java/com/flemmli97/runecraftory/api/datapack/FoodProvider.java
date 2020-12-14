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

public abstract class FoodProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(Attribute.class, new AttributeSerializer()).create();

    private final Map<ResourceLocation, FoodProperties.MutableFoodProps> data = new HashMap<>();
    private final DataGenerator gen;

    public FoodProvider(DataGenerator gen) {
        this.gen = gen;
    }

    protected abstract void addStats();

    @Override
    public void act(DirectoryCache cache) {
        this.addStats();
        data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/food_stats/" + res.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, GSON.toJsonTree(builder), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save food properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "FoodProps";
    }

    public void addStat(Item item, int duration) {
        this.data.put(item.getRegistryName(), new FoodProperties.MutableFoodProps(duration));
    }

    public void addStat(Item item, FoodProperties.MutableFoodProps builder) {
        this.data.put(item.getRegistryName(), builder);
    }
}
