package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CropManager extends SimpleJsonResourceReloadListener implements SyncableListener<Map<Item, CropProperties>> {

    public static final ResourceLocation ID = RuneCraftory.modRes("crop_properties");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Item, CropProperties>> CODEC = new StreamCodec<>() {
        @Override
        public Map<Item, CropProperties> decode(RegistryFriendlyByteBuf buf) {
            ImmutableMap.Builder<Item, CropProperties> builder = ImmutableMap.builder();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++)
                builder.put(ByteBufCodecs.registry(Registries.ITEM).decode(buf), CropProperties.STREAM_CODEC.decode(buf));
            return builder.build();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Map<Item, CropProperties> props) {
            buf.writeInt(props.size());
            props.forEach((item, prop) -> {
                ByteBufCodecs.registry(Registries.ITEM).encode(buf, item);
                CropProperties.STREAM_CODEC.encode(buf, prop);
            });
        }
    };

    private Map<Item, CropProperties> crops = ImmutableMap.of();
    private boolean resolved;
    private Map<TagKey<Item>, CropProperties> tagCrops = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public CropManager() {
        super(DataPackHandler.GSON, DIRECTORY);
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
                    .forEach(entry -> HolderUtils.expandTag(this.provider, Registries.ITEM, entry.getKey()).forEach(item -> {
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
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String key = GsonHelper.getAsString(obj, "item");
                if (key.startsWith("#")) {
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.parse(key.substring(1)));
                    CropProperties props = CropProperties.CODEC.parse(ops, el).getOrThrow();
                    props.setID(fres);
                    tagEntries.put(tag, props);
                } else {
                    Optional<Item> item = HolderUtils.get(this.provider, Registries.ITEM, ResourceLocation.parse(key));
                    item.ifPresent(i -> {
                        CropProperties props = CropProperties.CODEC.parse(ops, el).getOrThrow();
                        props.setID(fres);
                        itemEntries.put(i, props);
                    });
                }
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse crop properties json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.crops = itemEntries.build();
        this.tagCrops = tagEntries.build();
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
    public StreamCodec<RegistryFriendlyByteBuf, Map<Item, CropProperties>> codec() {
        return CODEC;
    }

    @Override
    public Map<Item, CropProperties> toSync() {
        this.resolveTags(false);
        return Collections.unmodifiableMap(this.crops);
    }

    @Override
    public void update(HolderLookup.Provider provider, Map<Item, CropProperties> value) {
        this.insertRegistryAccess(provider);
        this.crops = value;
    }
}
