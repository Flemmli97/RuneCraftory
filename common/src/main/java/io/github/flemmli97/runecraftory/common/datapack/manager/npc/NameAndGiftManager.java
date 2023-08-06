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

public class NameAndGiftManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonArray>> {

    private static final Gson GSON = new GsonBuilder().create();

    private static final List<ResourceLocation> resources = List.of(new ResourceLocation(RuneCraftory.MODID, "names"),
            new ResourceLocation(RuneCraftory.MODID, "surnames"),
            new ResourceLocation(RuneCraftory.MODID, "gifts"));

    public static final String DIRECTORY = "names_and_gifts";

    private List<String> surnames;
    private List<String> maleNames;
    private List<String> femaleNames;
    private Map<NPCData.GiftType, List<TagKey<Item>>> giftTags;

    public String getRandomSurname(Random random) {
        if (this.surnames.isEmpty())
            return null;
        return this.surnames.get(random.nextInt(this.surnames.size()));
    }

    public String getRandomName(Random random, boolean male) {
        if (male) {
            if (this.maleNames.isEmpty())
                return null;
            return this.maleNames.get(random.nextInt(this.maleNames.size()));
        }
        if (this.femaleNames.isEmpty())
            return null;
        return this.femaleNames.get(random.nextInt(this.femaleNames.size()));
    }

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
            path = path.substring(i, path.length() - 5);
            if (path.equals("male_names") || path.equals("female_names") || path.equals("surnames") || path.endsWith("_gifts")) {
                ResourceLocation res = new ResourceLocation(fileRes.getNamespace(), path);
                try {
                    try (Resource resource = resourceManager.getResource(fileRes)) {
                        try (InputStream inputStream = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                            JsonArray array = GsonHelper.fromJson(GSON, reader, JsonArray.class);
                            if (array != null) {
                                map.put(res, array);
                            }
                        }
                    }
                } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                    RuneCraftory.logger.error("Couldn't parse data file {} {}", res, exception);
                }
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonArray> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableList.Builder<String> surnames = ImmutableList.builder();
        ImmutableList.Builder<String> maleNames = ImmutableList.builder();
        ImmutableList.Builder<String> femaleNames = ImmutableList.builder();
        ImmutableList.Builder<Pair<NPCData.GiftType, TagKey<Item>>> gifts = ImmutableList.builder();
        data.forEach((res, arr) -> {
            if (res.getPath().endsWith("_gifts")) {
                String type = res.getPath().replace("_gifts", "");
                try {
                    NPCData.GiftType giftType = NPCData.GiftType.valueOf(type.toUpperCase(Locale.ROOT));
                    arr.forEach(e -> gifts.add(Pair.of(giftType, PlatformUtils.INSTANCE.itemTag(new ResourceLocation(e.getAsString())))));
                } catch (IllegalArgumentException ignored) {
                }
            } else {
                ImmutableList.Builder<String> list = switch (res.getPath()) {
                    case "surnames" -> surnames;
                    case "male_names" -> maleNames;
                    case "female_names" -> femaleNames;
                    default -> null;
                };
                if (list != null)
                    arr.forEach(e -> list.add(e.getAsString()));
            }
        });
        this.surnames = surnames.build();
        this.maleNames = maleNames.build();
        this.femaleNames = femaleNames.build();
        this.giftTags = gifts.build().stream().collect(Collectors.groupingBy(Pair::getFirst, Collectors.mapping(Pair::getSecond, Collectors.toList())));
    }
}
