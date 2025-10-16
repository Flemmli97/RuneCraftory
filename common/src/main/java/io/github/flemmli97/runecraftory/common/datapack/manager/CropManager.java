package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.datapack.SyncableListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CropManager extends SimpleJsonResourceReloadListener implements SyncableListener<Map<Item, ReloadableHolder<CropProperties>>> {

    public static final ResourceLocation ID = RuneCraftory.modRes("crop_property");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private static final StreamCodec<RegistryFriendlyByteBuf, ReloadableHolder<CropProperties>> HOLDER_CODEC = ReloadableHolder.streamCodec(CropProperties.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Item, ReloadableHolder<CropProperties>>> CODEC = new StreamCodec<>() {
        @Override
        public Map<Item, ReloadableHolder<CropProperties>> decode(RegistryFriendlyByteBuf buf) {
            ImmutableMap.Builder<Item, ReloadableHolder<CropProperties>> builder = ImmutableMap.builder();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++)
                builder.put(ByteBufCodecs.registry(Registries.ITEM).decode(buf), HOLDER_CODEC.decode(buf));
            return builder.build();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Map<Item, ReloadableHolder<CropProperties>> props) {
            buf.writeVarInt(props.size());
            props.forEach((item, prop) -> {
                ByteBufCodecs.registry(Registries.ITEM).encode(buf, item);
                HOLDER_CODEC.encode(buf, prop);
            });
        }
    };

    private Map<Item, ReloadableHolder<CropProperties>> itemLookup = ImmutableMap.of();
    private Map<Item, ReloadableHolder<CropProperties>> seedLookup = ImmutableMap.of();
    private Map<Block, ReloadableHolder<CropProperties>> blockLookup = ImmutableMap.of();
    private boolean resolved;
    private Set<ReloadableHolder<CropProperties>> unresolved = ImmutableSet.of();

    private HolderLookup.Provider provider;

    public CropManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    @Nullable
    public CropProperties get(Item item) {
        ReloadableHolder<CropProperties> value = this.getWithId(item);
        return value != null ? value.value() : null;
    }

    @Nullable
    public ReloadableHolder<CropProperties> getWithId(Item item) {
        if (GeneralConfig.disableCropSystem)
            return null;
        this.resolveTags(false);
        return this.itemLookup.get(item);
    }

    @Nullable
    public CropProperties get(Block block) {
        ReloadableHolder<CropProperties> value = this.getWithId(block);
        return value != null ? value.value() : null;
    }

    @Nullable
    public ReloadableHolder<CropProperties> getWithId(Block block) {
        if (GeneralConfig.disableCropSystem)
            return null;
        this.resolveTags(false);
        return this.blockLookup.get(block);
    }

    @Nullable
    public ReloadableHolder<CropProperties> getSeedWithId(Item item) {
        if (GeneralConfig.disableCropSystem)
            return null;
        this.resolveTags(false);
        return this.seedLookup.get(item);
    }

    public void resolveTags(boolean forced) {
        if (!this.resolved || forced) {
            this.resolved = true;
            HashMap<Item, ReloadableHolder<CropProperties>> tagEntries = new HashMap<>();
            HashMap<Item, ReloadableHolder<CropProperties>> itemEntries = new HashMap<>();
            HashMap<Block, ReloadableHolder<CropProperties>> tagEntriesBlocks = new HashMap<>();
            HashMap<Block, ReloadableHolder<CropProperties>> itemEntriesBlocks = new HashMap<>();
            // Tags have lower priorities
            this.unresolved.stream().sorted(Comparator.comparing(ReloadableHolder::id)).forEach(entry -> {
                CropProperties.CropMappingInfo info = entry.value().getInfo();
                if (info.seed() instanceof HolderSet.Named<Item>) {
                    info.seed().forEach(h -> tagEntries.put(h.value(), entry));
                } else {
                    info.seed().forEach(h -> itemEntries.put(h.value(), entry));
                }
                if (info.crop() instanceof HolderSet.Named<Item>) {
                    info.crop().forEach(h -> tagEntries.put(h.value(), entry));
                } else {
                    info.crop().forEach(h -> itemEntries.put(h.value(), entry));
                }
                if (info.cropBlock() instanceof HolderSet.Named<Block>) {
                    info.cropBlock().forEach(h -> tagEntriesBlocks.put(h.value(), entry));
                } else {
                    info.cropBlock().forEach(h -> itemEntriesBlocks.put(h.value(), entry));
                }
                info.giant().ifPresent(giant -> {
                    itemEntries.put(giant.getFirst(), entry);
                    itemEntriesBlocks.put(giant.getSecond(), entry);
                });
            });
            tagEntries.putAll(itemEntries);
            this.itemLookup = ImmutableMap.copyOf(tagEntries);
            // For client syncing. Client only needs to know seeds for tooltip displays
            HashMap<Item, ReloadableHolder<CropProperties>> seedEntries = new HashMap<>();
            this.itemLookup.forEach((item, prop) -> {
                if (prop.value().getInfo().seed().contains(BuiltInRegistries.ITEM.wrapAsHolder(item))) {
                    seedEntries.put(item, prop);
                }
            });
            this.seedLookup = ImmutableMap.copyOf(seedEntries);
            tagEntriesBlocks.putAll(itemEntriesBlocks);
            this.blockLookup = ImmutableMap.copyOf(tagEntriesBlocks);
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        this.resolved = false;
        ImmutableSet.Builder<ReloadableHolder<CropProperties>> toResolve = ImmutableSet.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                CropProperties props = CropProperties.CODEC.parse(ops, el).getOrThrow();
                toResolve.add(new ReloadableHolder<>(fres, props));
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse crop properties json {} {}", fres, ex, ex.fillInStackTrace());
            }
        });
        this.unresolved = toResolve.build();
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
    public StreamCodec<RegistryFriendlyByteBuf, Map<Item, ReloadableHolder<CropProperties>>> codec() {
        return CODEC;
    }

    @Override
    public Map<Item, ReloadableHolder<CropProperties>> toSync() {
        this.resolveTags(false);
        return Collections.unmodifiableMap(this.seedLookup);
    }

    @Override
    public void update(HolderLookup.Provider provider, Map<Item, ReloadableHolder<CropProperties>> value) {
        this.insertRegistryAccess(provider);
        this.seedLookup = value;
    }
}
