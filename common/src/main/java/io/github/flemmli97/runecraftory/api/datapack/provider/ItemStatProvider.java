package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
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

public abstract class ItemStatProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, ItemStat.Builder> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public ItemStatProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + ItemStatManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = ItemStat.CODEC.encodeStart(JsonOps.INSTANCE, builder.build())
                        .getOrThrow(false, RuneCraftory.logger::error);
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                DataProvider.save(GsonInstances.ATTRIBUTE_SPELLS, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "ItemStats";
    }

    public void addStat(ItemLike item, int buy, int sell, int upgrade) {
        this.addStat(item, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(String id, ItemLike item, int buy, int sell, int upgrade) {
        this.addStat(id, item, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(ItemLike item, ItemStat.Builder builder) {
        this.addStat(PlatformUtils.INSTANCE.items().getIDFrom(item.asItem()).getPath(), item, builder);
    }

    public void addStat(String id, ItemLike item, ItemStat.Builder builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", (PlatformUtils.INSTANCE.items().getIDFrom(item.asItem()).toString())));
    }

    public void addStat(String id, TagKey<Item> tag, int buy, int sell, int upgrade) {
        this.addStat(id, tag, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(String id, TagKey<Item> tag, ItemStat.Builder builder) {
        ResourceLocation res = new ResourceLocation(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", tag.location().toString()));
    }
}
