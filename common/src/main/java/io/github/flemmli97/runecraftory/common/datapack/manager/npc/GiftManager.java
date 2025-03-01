package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.npc.GiftData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class GiftManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "npc_gifts";

    private Map<ResourceLocation, GiftData> gifts;
    private Map<GiftData, ResourceLocation> giftsLookup;
    private List<GiftData> giftsList;

    public GiftManager() {
        super(GsonInstances.GSON, DIRECTORY);
    }

    public GiftData get(ResourceLocation id) {
        return this.gifts.get(id);
    }

    public ResourceLocation getId(GiftData data) {
        return this.giftsLookup.get(data);
    }

    public GiftData getRandomGift(Random random, int xp) {
        List<GiftData> selectables = this.giftsList.stream().filter(g -> g.matches(xp)).toList();
        if (selectables.isEmpty())
            return null;
        return selectables.get(random.nextInt(selectables.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, GiftData> builder = ImmutableMap.builder();
        data.forEach((res, e) -> {
            try {
                builder.put(res, GiftData.CODEC.parse(JsonOps.INSTANCE, e)
                        .getOrThrow(true, RuneCraftory.LOGGER::error));
            } catch (Exception exception) {
                RuneCraftory.LOGGER.error("Error parsing GiftData: {} - {}", res, exception);
            }
        });
        this.gifts = builder.build();
        ImmutableMap.Builder<GiftData, ResourceLocation> reverse = ImmutableMap.builder();
        this.gifts.forEach((resourceLocation, giftData) -> reverse.put(giftData, resourceLocation));
        this.giftsLookup = reverse.build();
        this.giftsList = this.gifts.keySet().stream().sorted().map(this.gifts::get).toList();
    }
}
