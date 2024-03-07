package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GiftManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonArray>> {

    private static final Gson GSON = new GsonBuilder().create();

    public static final String DIRECTORY = "npc_gifts";

    private Map<NPCData.GiftType, List<TagKey<Item>>> giftTags;

    public TagKey<Item> getRandomGift(NPCData.GiftType type, Random random) {
        if (type == NPCData.GiftType.NEUTRAL)
            return null;
        List<TagKey<Item>> list = this.giftTags.get(type);
        if (list == null || list.isEmpty())
            return null;
        return list.get(random.nextInt(list.size()));
    }

    @Override
    protected Map<ResourceLocation, JsonArray> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        HashMap<ResourceLocation, JsonArray> map = Maps.newHashMap();
        int i = DIRECTORY.length() + 1;
        for (ResourceLocation fileRes : resourceManager.listResources(DIRECTORY, string -> string.endsWith(".json"))) {
            String path = fileRes.getPath();
            path = path.substring(i, path.length() - ".json".length());
            ResourceLocation res = new ResourceLocation(fileRes.getNamespace(), path);
            try {
                try (Resource resource = resourceManager.getResource(fileRes)) {
                    try (InputStream inputStream = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        JsonArray arr = GsonHelper.fromJson(GSON, reader, JsonArray.class);
                        if (arr != null) {
                            map.put(res, arr);
                        }
                    }
                }
            } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                RuneCraftory.LOGGER.error("Couldn't parse gift file {} {}", res, exception);
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonArray> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableList.Builder<Pair<NPCData.GiftType, TagKey<Item>>> gifts = ImmutableList.builder();
        data.forEach((res, arr) -> {
            try {
                NPCData.GiftType giftType = NPCData.GiftType.valueOf(res.getPath().toUpperCase(Locale.ROOT));
                arr.forEach(e -> gifts.add(Pair.of(giftType, PlatformUtils.INSTANCE.itemTag(new ResourceLocation(e.getAsString())))));
            } catch (IllegalArgumentException ignored) {
                RuneCraftory.LOGGER.error("No such gift type {}", res.getPath());
            }
        });
        this.giftTags = gifts.build().stream().collect(Collectors.groupingBy(Pair::getFirst, Collectors.mapping(Pair::getSecond, Collectors.toList())));
    }
}
