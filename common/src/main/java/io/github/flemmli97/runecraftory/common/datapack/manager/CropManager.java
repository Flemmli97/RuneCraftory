package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.utils.MiscUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CropManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "crop_properties";
    private static final Gson GSON = new GsonBuilder().create();

    private Map<Item, CropProperties> crops = ImmutableMap.of();
    private boolean resolved;
    private Map<TagKey<Item>, CropProperties> tagCrops = ImmutableMap.of();

    public CropManager() {
        super(GSON, DIRECTORY);
    }

    @Nullable
    public CropProperties get(Item item) {
        if (GeneralConfig.disableCropSystem)
            return null;
        this.resolveTags(false);
        return this.crops.get(item);
    }

    public void resolveTags(boolean forced) {
        if (!this.resolved || forced) {
            this.resolved = true;
            HashMap<Item, CropProperties> itemEntries = new HashMap<>(this.crops);
            this.tagCrops.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().location()))
                    .forEach(entry -> MiscUtils.expandTag(Registry.ITEM, entry.getKey()).forEach(item -> {
                        if (!itemEntries.containsKey(item))
                            itemEntries.put(item, entry.getValue());
                    }));
            this.crops = ImmutableMap.copyOf(itemEntries);
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        this.resolved = false;
        ImmutableMap.Builder<Item, CropProperties> itemEntries = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, CropProperties> tagEntries = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String key = GsonHelper.getAsString(obj, "item");
                if (key.startsWith("#")) {
                    TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(key.substring(1)));
                    CropProperties props = CropProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.LOGGER::error);
                    props.setID(fres);
                    tagEntries.put(tag, props);
                } else {
                    Item item = Registry.ITEM.get(new ResourceLocation(key));
                    if (item != Items.AIR) {
                        CropProperties props = CropProperties.CODEC.parse(JsonOps.INSTANCE, el)
                                .getOrThrow(false, RuneCraftory.LOGGER::error);
                        props.setID(fres);
                        itemEntries.put(item, props);
                    }
                }
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse crop properties json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.crops = itemEntries.build();
        this.tagCrops = tagEntries.build();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        this.resolveTags(false);
        buffer.writeInt(this.crops.size());
        this.crops.forEach((item, prop) -> {
            buffer.writeResourceLocation(Registry.ITEM.getKey(item));
            prop.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<Item, CropProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(Registry.ITEM.get(buffer.readResourceLocation()), CropProperties.fromPacket(buffer));
        this.crops = builder.build();
    }
}
