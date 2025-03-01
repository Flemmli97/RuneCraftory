package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
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

public class FoodManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "food_stats";

    private Map<Item, FoodProperties> food = ImmutableMap.of();
    private boolean resolved;
    private Map<TagKey<Item>, FoodProperties> tagFood = ImmutableMap.of();

    public FoodManager() {
        super(GsonInstances.GSON, DIRECTORY);
    }

    @Nullable
    public FoodProperties get(Item item) {
        if (GeneralConfig.disableFoodSystem)
            return null;
        this.resolveTags(false);
        return this.food.get(item);
    }

    public void resolveTags(boolean forced) {
        if (!this.resolved || forced) {
            this.resolved = true;
            HashMap<Item, FoodProperties> itemEntries = new HashMap<>(this.food);
            this.tagFood.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().location()))
                    .forEach(entry -> MiscUtils.expandTag(Registry.ITEM, entry.getKey()).forEach(item -> {
                        if (!itemEntries.containsKey(item))
                            itemEntries.put(item, entry.getValue());
                    }));
            this.food = ImmutableMap.copyOf(itemEntries);
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        this.resolved = false;
        ImmutableMap.Builder<Item, FoodProperties> itemEntries = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, FoodProperties> tagEntries = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String key = GsonHelper.getAsString(obj, "item");
                if (key.startsWith("#")) {
                    TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(key.substring(1)));
                    FoodProperties props = FoodProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.LOGGER::error);
                    props.setID(fres);
                    tagEntries.put(tag, props);
                } else {
                    Item item = Registry.ITEM.get(new ResourceLocation(key));
                    if (item != Items.AIR) {
                        FoodProperties props = FoodProperties.CODEC.parse(JsonOps.INSTANCE, el)
                                .getOrThrow(false, RuneCraftory.LOGGER::error);
                        props.setID(fres);
                        itemEntries.put(item, props);
                    }
                }
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse food stat json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.food = itemEntries.build();
        this.tagFood = tagEntries.build();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        this.resolveTags(false);
        buffer.writeInt(this.food.size());
        this.food.forEach((item, prop) -> {
            buffer.writeResourceLocation(Registry.ITEM.getKey(item));
            prop.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<Item, FoodProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(Registry.ITEM.get(buffer.readResourceLocation()), FoodProperties.fromPacket(buffer));
        this.food = builder.build();
    }
}
