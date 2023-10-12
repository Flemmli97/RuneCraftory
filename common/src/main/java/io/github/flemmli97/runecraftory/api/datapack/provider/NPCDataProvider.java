package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCActionManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameAndGiftManager;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class NPCDataProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<NPCData.GiftType, List<ResourceLocation>> gifts = new HashMap<>();
    private final Map<ResourceLocation, NPCData> data = new HashMap<>();
    private final Map<ResourceLocation, NPCData.NPCLook> looks = new HashMap<>();
    private final Map<ResourceLocation, NPCData.ConversationSet> conversations = new HashMap<>();
    private final Map<ResourceLocation, NPCAttackActions> actions = new HashMap<>();

    private List<String> surnames = new ArrayList<>();
    private List<String> maleNames = new ArrayList<>();
    private List<String> femaleNames = new ArrayList<>();
    private Map<NPCData.GiftType, List<TagKey<Item>>> giftTags = new LinkedHashMap<>();

    //Translation for lang
    public final Map<String, String> translations = new LinkedHashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public NPCDataProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.gifts.forEach((type, giftList) -> {
            JsonArray giftArr = new JsonArray();
            giftList.forEach(r -> giftArr.add(r.toString()));
            Path path1 = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + NameAndGiftManager.DIRECTORY + "/" + type.name().toLowerCase(Locale.ROOT) + "_gifts.json");
            try {
                DataProvider.save(GSON, cache, giftArr, path1);
            } catch (IOException e) {
                LOGGER.error("Couldn't save gifts {}", path1, e);
            }
        });

        this.data.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCDataManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCData.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc data {}", path, e);
            }
        });
        this.looks.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCLookManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCData.NPCLook.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc looks {}", path, e);
            }
        });
        this.conversations.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCConversationManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCData.ConversationSet.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc conversations {}", path, e);
            }
        });
        Path path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + NameAndGiftManager.DIRECTORY + "/female_names.json");
        try {
            JsonElement obj = GSON.toJsonTree(this.femaleNames);
            DataProvider.save(GSON, cache, obj, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save male names {}", path, e);
        }
        path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + NameAndGiftManager.DIRECTORY + "/male_names.json");
        try {
            JsonElement obj = GSON.toJsonTree(this.maleNames);
            DataProvider.save(GSON, cache, obj, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save female names {}", path, e);
        }
        path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + NameAndGiftManager.DIRECTORY + "/surnames.json");
        try {
            JsonElement obj = GSON.toJsonTree(this.surnames);
            DataProvider.save(GSON, cache, obj, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save surnames {}", path, e);
        }
        this.gifts.forEach((type, list) -> {
            Path path1 = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + NameAndGiftManager.DIRECTORY + "/" + type.name().toLowerCase() + "_gifts.json");
            try {
                JsonElement obj = GSON.toJsonTree(list);
                DataProvider.save(GSON, cache, obj, path1);
            } catch (IOException e) {
                LOGGER.error("Couldn't save gifts {}", path1, e);
            }
        });
        this.actions.forEach((res, val) -> {
            Path path1 = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCActionManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCAttackActions.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GSON, cache, obj, path1);
            } catch (IOException e) {
                LOGGER.error("Couldn't combat action {}", path1, e);
            }
        });
    }

    @Override
    public String getName() {
        return "NPCData";
    }

    public void addNPCData(String id, NPCData.Builder data) {
        //if (data.look() != null && !this.looks.containsKey(data.look()))
        //    throw new IllegalStateException("NPC has look defined but there is no such look registered");
        this.translations.putAll(data.getTranslations());
        this.data.put(new ResourceLocation(this.modid, id), data.build());
    }

    public void addNPCData(String id, NPCData.Builder data, Map<NPCData.ConversationType, NPCData.ConversationSet.Builder> conversations,
                           Map<ResourceLocation, QuestResponseBuilder> questConversations) {
        //if (data.look() != null && !this.looks.containsKey(data.look()))
        //    throw new IllegalStateException("NPC has look defined but there is no such look registered");
        conversations.forEach((key, value) -> {
            ResourceLocation conversationId = new ResourceLocation(this.modid, id + "/" + key.key);
            this.translations.putAll(value.getTranslations());
            this.conversations.put(conversationId, value.build());
            data.addInteractionIfAbsent(key, conversationId);
        });
        questConversations.forEach((key, value) -> {
            ResourceLocation startId = new ResourceLocation(this.modid, id + "/quest_start_" + key.getPath());
            this.translations.putAll(value.start.getTranslations());
            this.conversations.put(startId, value.start.build());
            ResourceLocation runId = new ResourceLocation(this.modid, id + "/quest_active_" + key.getPath());
            this.translations.putAll(value.active.getTranslations());
            this.conversations.put(runId, value.active.build());
            ResourceLocation endId = new ResourceLocation(this.modid, id + "/quest_end_" + key.getPath());
            this.translations.putAll(value.end.getTranslations());
            this.conversations.put(endId, value.end.build());
            data.addQuestResponse(key, startId, runId, endId);
        });
        this.translations.putAll(data.getTranslations());
        this.data.put(new ResourceLocation(this.modid, id), data.build());
    }

    public void addNPCDataWithLook(String id, NPCData.Builder builder, NPCData.NPCLook look) {
        this.translations.putAll(builder.getTranslations());
        NPCData data = builder.build();
        this.data.put(new ResourceLocation(this.modid, id), data);
        this.looks.put(data.look(), look);
    }

    public void addNPCDataAll(String id, NPCData.Builder data, Map<NPCData.ConversationType, NPCData.ConversationSet.Builder> conversations, NPCData.NPCLook look) {
        conversations.forEach((key, value) -> {
            ResourceLocation conversationId = new ResourceLocation(this.modid, id + "/" + key.key);
            this.translations.putAll(value.getTranslations());
            this.conversations.put(conversationId, value.build());
            data.addInteractionIfAbsent(key, conversationId);
        });
        this.translations.putAll(data.getTranslations());
        NPCData npcData = data.build();
        this.data.put(new ResourceLocation(this.modid, id), npcData);
        this.looks.put(npcData.look(), look);
    }

    public void addSelectableGiftTag(NPCData.GiftType type, TagKey<Item> tag) {
        this.addSelectableGiftTag(type, tag.location());
    }

    public void addSelectableGiftTag(NPCData.GiftType type, ResourceLocation tag) {
        this.gifts.computeIfAbsent(type, t -> new ArrayList<>()).add(tag);
    }

    public void addFemaleName(String name) {
        this.femaleNames.add(name);
    }

    public void addMaleName(String name) {
        this.maleNames.add(name);
    }

    public void addSurname(String name) {
        this.surnames.add(name);
    }

    public void addGenericGift(NPCData.GiftType type, TagKey<Item> tag) {
        this.gifts.computeIfAbsent(type, r -> new ArrayList<>()).add(tag.location());
    }

    public ResourceLocation addAttackActions(ResourceLocation id, NPCAttackActions.Builder actions) {
        this.actions.put(id, actions.build());
        return id;
    }

    public record QuestResponseBuilder(NPCData.ConversationSet.Builder start, NPCData.ConversationSet.Builder active,
                                       NPCData.ConversationSet.Builder end) {
    }
}
