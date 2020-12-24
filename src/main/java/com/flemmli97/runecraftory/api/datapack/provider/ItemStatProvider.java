package com.flemmli97.runecraftory.api.datapack.provider;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
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
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ItemStatProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(ForgeRegistries.ATTRIBUTES))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(RegistryManager.ACTIVE.getRegistry(Spell.class))).create();

    private final Map<ResourceLocation, ItemStat.MutableItemStat> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public ItemStatProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void act(DirectoryCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/item_stats/" + res.getPath() + ".json");
            try {
                JsonElement obj = GSON.toJsonTree(builder);
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                IDataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "ItemStats";
    }

    public void addStat(IItemProvider item, int buy, int sell, int upgrade) {
        this.addStat(item, new ItemStat.MutableItemStat(buy, sell, upgrade));
    }

    public void addStat(String id, IItemProvider item, int buy, int sell, int upgrade) {
        this.addStat(id, item, new ItemStat.MutableItemStat(buy, sell, upgrade));
    }

    public void addStat(IItemProvider item, ItemStat.MutableItemStat builder) {
        this.addStat(item.asItem().getRegistryName().getPath(), item, builder);
    }

    public void addStat(String id, IItemProvider item, ItemStat.MutableItemStat builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", item.asItem().getRegistryName().toString()));
    }

    public void addStat(String id, ITag<Item> tag, int buy, int sell, int upgrade) {
        this.addStat(id, tag, new ItemStat.MutableItemStat(buy, sell, upgrade));
    }

    public void addStat(String id, ITag<Item> tag, ItemStat.MutableItemStat builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("tag", TagCollectionManager.getTagManager().getItems().getTagId(tag).toString()));
    }
}
