package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FarmlandHoeTileActionManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("farmland_hoe_tile_action");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<Item, Data> tileActions = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public FarmlandHoeTileActionManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    @Nullable
    public Data get(Item item) {
        return this.tileActions.get(item);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<Item, Data> entries = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                Data val = Data.CODEC.parse(ops, el).getOrThrow();
                entries.put(val.item(), val);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse hoe tile action json {} {}", fres, ex, ex.fillInStackTrace());
            }
        });
        this.tileActions = entries.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public record Data(Item item, int amount) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(Data::item),
                        Codec.INT.fieldOf("amount").forGetter(Data::amount)
                ).apply(instance, Data::new));
    }
}
