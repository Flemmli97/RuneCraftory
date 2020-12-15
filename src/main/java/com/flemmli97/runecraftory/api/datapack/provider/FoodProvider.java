package com.flemmli97.runecraftory.api.datapack.provider;

import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class FoodProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(ForgeRegistries.ATTRIBUTES))
            .registerTypeAdapter(Effect.class, new RegistryObjectSerializer<>(ForgeRegistries.POTIONS)).create();

    private final Map<ResourceLocation, FoodProperties.MutableFoodProps> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public FoodProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void act(DirectoryCache cache) {
        this.add();
        data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/food_stats/" + res.getPath() + ".json");
            try {
                JsonElement obj = GSON.toJsonTree(builder);
                if (obj.isJsonObject())
                    item.get(res).accept(obj.getAsJsonObject());
                IDataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save food properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "FoodProps";
    }

    public void addStat(String id, Item item, int duration) {
        this.addStat(id, item, new FoodProperties.MutableFoodProps(duration));
    }

    public void addStat(String id, Item item, FoodProperties.MutableFoodProps builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", item.getRegistryName().toString()));
    }

    public void addStat(String id, ITag<Item> tag, int duration) {
        this.addStat(id, tag, new FoodProperties.MutableFoodProps(duration));
    }

    public void addStat(String id, ITag<Item> tag, FoodProperties.MutableFoodProps builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("tag", TagCollectionManager.getTagManager().getItems().getTagId(tag).toString()));
    }
}
