package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.SyncableListener;
import io.github.flemmli97.runecraftory.common.utils.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemStatManager extends SimpleJsonResourceReloadListener implements SyncableListener<Map<Item, ItemStat>> {

    public static final ResourceLocation ID = RuneCraftory.modRes("item_stats");

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Item, ItemStat>> CODEC = new StreamCodec<>() {
        @Override
        public Map<Item, ItemStat> decode(RegistryFriendlyByteBuf buf) {
            ImmutableMap.Builder<Item, ItemStat> builder = ImmutableMap.builder();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++)
                builder.put(ByteBufCodecs.registry(Registries.ITEM).decode(buf), ItemStat.fromPacket(buf));
            return builder.build();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Map<Item, ItemStat> crops) {
            buf.writeInt(crops.size());
            crops.forEach((item, prop) -> {
                ByteBufCodecs.registry(Registries.ITEM).encode(buf, item);
                prop.toPacket(buf);
            });
        }
    };

    private Map<Item, ItemStat> itemstats = ImmutableMap.of();
    private boolean resolved;
    private Map<TagKey<Item>, ItemStat> tagStats = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public ItemStatManager() {
        super(DataPackHandler.GSON, ID.toString());
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
                    .forEach(entry -> HolderUtils.expandTag(this.provider, Registries.ITEM, entry.getKey()).forEach(item -> {
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
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String key = GsonHelper.getAsString(obj, "item");
                if (key.startsWith("#")) {
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.parse(key.substring(1)));
                    ItemStat props = ItemStat.CODEC.parse(ops, el).getOrThrow();
                    props.setID(fres);
                    tagEntries.put(tag, props);
                } else {
                    Optional<Item> item = HolderUtils.get(this.provider, Registries.ITEM, ResourceLocation.parse(key));
                    item.ifPresent(i -> {
                        ItemStat props = ItemStat.CODEC.parse(ops, el).getOrThrow();
                        props.setID(fres);
                        itemEntries.put(i, props);
                    });
                }
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse item stat json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.itemstats = itemEntries.build();
        this.tagStats = tagEntries.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Map<Item, ItemStat>> codec() {
        return CODEC;
    }

    @Override
    public Map<Item, ItemStat> toSync() {
        this.resolveTags(false);
        return Collections.unmodifiableMap(this.itemstats);
    }

    @Override
    public void update(HolderLookup.Provider provider, Map<Item, ItemStat> value) {
        this.insertRegistryAccess(provider);
        this.itemstats = value;
    }
}
