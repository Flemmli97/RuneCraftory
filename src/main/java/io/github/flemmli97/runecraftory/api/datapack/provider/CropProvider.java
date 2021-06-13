package io.github.flemmli97.runecraftory.api.datapack.provider;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CropProvider implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, CropProperties.MutableCropProps> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public CropProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void act(DirectoryCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/crop_properties/" + res.getPath() + ".json");
            try {
                JsonElement obj = GSON.toJsonTree(builder);
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                IDataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save crop properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "CropProps";
    }

    public void addStat(Item item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(item, new CropProperties.MutableCropProps(growth, maxDrops, regrowable));
    }

    public void addStat(String id, Item item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, item, new CropProperties.MutableCropProps(growth, maxDrops, regrowable));
    }

    public void addStat(Item item, CropProperties.MutableCropProps builder) {
        this.addStat(item.getRegistryName().getPath(), item, builder);
    }

    public void addStat(String id, Item item, CropProperties.MutableCropProps builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", item.getRegistryName().toString()));
    }

    public void addStat(String id, ITag<Item> tag, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, tag, new CropProperties.MutableCropProps(growth, maxDrops, regrowable));
    }

    public void addStat(String id, ITag<Item> tag, CropProperties.MutableCropProps builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("tag", TagCollectionManager.getManager().getItemTags().getValidatedIdFromTag(tag).toString()));
    }
}
