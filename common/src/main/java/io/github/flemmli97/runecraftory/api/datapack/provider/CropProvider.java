package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CropProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, CropProperties.Builder> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public CropProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + CropManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = CropProperties.CODEC.encodeStart(JsonOps.INSTANCE, builder.build())
                        .getOrThrow(false, LOGGER::error);
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save crop properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "CropProps";
    }

    public void addStat(ItemLike item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(item, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(String id, ItemLike item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, item, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(ItemLike item, CropProperties.Builder builder) {
        this.addStat(PlatformUtils.INSTANCE.items().getIDFrom(item.asItem()).getPath(), item, builder);
    }

    public void addStat(String id, ItemLike item, CropProperties.Builder builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", (PlatformUtils.INSTANCE.items().getIDFrom(item.asItem()).toString())));
    }

    public void addStat(String id, TagKey<Item> tag, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, tag, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(String id, TagKey<Item> tag, CropProperties.Builder builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("tag", tag.location().toString()));
    }
}
