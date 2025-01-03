package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemStatManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "item_stats";

    private Map<Item, ItemStat> itemstats = ImmutableMap.of();
    private boolean resolved;
    private Map<TagKey<Item>, ItemStat> tagStats = ImmutableMap.of();

    public ItemStatManager() {
        super(GsonInstances.ATTRIBUTE_SPELLS, DIRECTORY);
    }

    public Optional<ItemStat> get(Item item) {
        if (GeneralConfig.disableItemStatSystem)
            return Optional.empty();
        this.resolveTags(false);
        return Optional.ofNullable(this.itemstats.get(item));
    }

    public List<Pair<ItemStack, ItemStat>> all() {
        return this.all(t -> true);
    }

    public List<Pair<ItemStack, ItemStat>> all(Predicate<ItemStack> test) {
        List<Pair<ItemStack, ItemStat>> list = new ArrayList<>();
        this.itemstats.forEach((item, stat) -> {
            ItemStack stack = new ItemStack(item);
            if (!stack.isEmpty() && test.test(stack))
                list.add(Pair.of(stack, stat));
        });
        return list;
    }

    public void resolveTags(boolean forced) {
        if (!this.resolved || forced) {
            this.resolved = true;
            HashMap<Item, ItemStat> itemEntries = new HashMap<>(this.itemstats);
            this.tagStats.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().location()))
                    .forEach(entry -> MiscUtils.expandTag(Registry.ITEM, entry.getKey()).forEach(item -> {
                        if (!itemEntries.containsKey(item))
                            itemEntries.put(item, entry.getValue());
                    }));
            this.itemstats = ImmutableMap.copyOf(itemEntries);
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        this.resolved = false;
        ImmutableMap.Builder<Item, ItemStat> itemEntries = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, ItemStat> tagEntries = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String key = GsonHelper.getAsString(obj, "item");
                if (key.startsWith("#")) {
                    TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(key.substring(1)));
                    ItemStat props = ItemStat.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.LOGGER::error);
                    props.setID(fres);
                    tagEntries.put(tag, props);
                } else {
                    Item item = Registry.ITEM.get(new ResourceLocation(key));
                    if (item != Items.AIR) {
                        ItemStat props = ItemStat.CODEC.parse(JsonOps.INSTANCE, el)
                                .getOrThrow(false, RuneCraftory.LOGGER::error);
                        props.setID(fres);
                        itemEntries.put(item, props);
                    }
                }
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse item stat json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.itemstats = itemEntries.build();
        this.tagStats = tagEntries.build();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        this.resolveTags(false);
        buffer.writeInt(this.itemstats.size());
        this.itemstats.forEach((item, prop) -> {
            buffer.writeResourceLocation(Registry.ITEM.getKey(item));
            prop.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<Item, ItemStat> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(Registry.ITEM.get(buffer.readResourceLocation()), ItemStat.fromPacket(buffer));
        this.itemstats = builder.build();
    }
}
