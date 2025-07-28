package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.GiftData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class GiftManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_gift");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<ResourceLocation, ReloadableHolder<GiftData>> gifts;
    private List<ReloadableHolder<GiftData>> giftsList;

    private HolderLookup.Provider provider;

    public GiftManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public ReloadableHolder<GiftData> get(ResourceLocation id) {
        return this.gifts.get(id);
    }

    public ReloadableHolder<GiftData> getRandomGift(Random random, int xp) {
        List<ReloadableHolder<GiftData>> selectables = this.giftsList.stream().filter(g -> g.value().matches(xp)).toList();
        if (selectables.isEmpty())
            return null;
        return selectables.get(random.nextInt(selectables.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ReloadableHolder<GiftData>> builder = ImmutableMap.builder();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((res, e) -> {
            try {
                builder.put(res, new ReloadableHolder<>(res, GiftData.CODEC.parse(ops, e).getOrThrow()));
            } catch (Exception exception) {
                RuneCraftory.LOGGER.error("Error parsing GiftData: {} - {}", res, exception);
            }
        });
        this.gifts = builder.build();
        this.giftsList = this.gifts.keySet().stream().sorted().map(this.gifts::get).toList();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }
}
