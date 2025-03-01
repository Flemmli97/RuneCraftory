package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.api.datapack.npc.GiftData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.GiftManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCActionManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class NPCDataProvider implements DataProvider, AdditionalLanguages {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, NPCLook> looks = new HashMap<>();
    private final Map<ResourceLocation, ConversationSet> conversations = new HashMap<>();
    private final Map<ResourceLocation, GiftData> giftData = new LinkedHashMap<>();
    private final Map<ResourceLocation, NPCAttackActions> actions = new HashMap<>();
    private final Map<ResourceLocation, NPCData> data = new HashMap<>();

    //Translation for lang
    public final Map<String, Map<String, String>> dialogueTranslations = new LinkedHashMap<>();
    private final Map<String, String> translations = new LinkedHashMap<>();

    private final DataGenerator gen;
    private final FileVerifier verifier;
    protected final String modid;

    public NPCDataProvider(DataGenerator gen, FileVerifier verifier, String modid) {
        this.gen = gen;
        this.verifier = verifier;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.data.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCDataManager.DIRECTORY + "/" + res.getPath() + ".json");
            this.verifyData(val);
            try {
                JsonElement obj = NPCData.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc data {}", path, e);
            }
        });
        this.looks.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCLookManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCLook.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc looks {}", path, e);
            }
        });
        this.conversations.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCConversationManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = ConversationSet.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save npc conversations {}", path, e);
            }
        });
        this.giftData.forEach((res, val) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + GiftManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = GiftData.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save gift data {}", path, e);
            }
        });
        this.actions.forEach((res, val) -> {
            Path path1 = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + NPCActionManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = NPCAttackActions.CODEC.encodeStart(JsonOps.INSTANCE, val)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.GSON, cache, obj, path1);
            } catch (IOException e) {
                LOGGER.error("Couldn't combat action {}", path1, e);
            }
        });
    }

    @Override
    public String getName() {
        return "NPCData";
    }

    public void addNPCData(String id, NPCData.Builder data, Map<ConversationContext, ConversationSet.Builder> conversations,
                           Map<ResourceLocation, QuestResponseBuilder> questConversations) {
        conversations.forEach((key, value) -> {
            ResourceLocation conversationId = new ResourceLocation(this.modid, id + "/" + key.key().getPath());
            this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                    .putAll(value.getTranslations());
            this.conversations.put(conversationId, value.build());
            data.addInteractionIfAbsent(key, conversationId);
        });
        questConversations.forEach((key, value) -> {
            ResourceLocation startId = new ResourceLocation(this.modid, id + "/quest_start_" + key.getPath());
            for (int i = 0; i < value.start.size(); i++) {
                String path = startId.getPath();
                if (i != 0)
                    path += "_" + i;
                ResourceLocation runIdI = new ResourceLocation(this.modid, path);
                this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                        .putAll(value.start.get(i).getTranslations());
                this.conversations.put(runIdI, value.start.get(i).build());
            }
            ResourceLocation runId = new ResourceLocation(this.modid, id + "/quest_active_" + key.getPath());
            for (int i = 0; i < value.active.size(); i++) {
                String path = runId.getPath();
                if (i != 0)
                    path += "_" + i;
                ResourceLocation runIdI = new ResourceLocation(this.modid, path);
                this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                        .putAll(value.active.get(i).getTranslations());
                this.conversations.put(runIdI, value.active.get(i).build());
            }
            ResourceLocation endId = new ResourceLocation(this.modid, id + "/quest_end_" + key.getPath());
            this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                    .putAll(value.end.getTranslations());
            this.conversations.put(endId, value.end.build());
            data.addQuestResponse(key, startId, runId, endId);
        });
        this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                .putAll(data.getTranslations());
        this.data.put(new ResourceLocation(this.modid, id), data.build());
    }

    public ResourceLocation addLook(ResourceLocation id, NPCLook look) {
        if (this.looks.put(id, look) != null)
            throw new IllegalStateException("Look already registered");
        this.verifier.track(id, PackType.SERVER_DATA, NPCLookManager.DIRECTORY);
        return id;
    }

    public ResourceLocation addGiftData(ResourceLocation id, GiftData.Builder giftData) {
        if (this.giftData.put(id, giftData.build()) != null)
            throw new IllegalStateException("GiftData already registered");
        this.translations.putAll(giftData.translations);
        this.verifier.track(id, PackType.SERVER_DATA, GiftManager.DIRECTORY);
        return id;
    }

    public ResourceLocation addAttackActions(ResourceLocation id, NPCAttackActions.Builder actions) {
        if (this.actions.put(id, actions.build()) != null)
            throw new IllegalStateException("Attack action already registered");
        this.verifier.track(id, PackType.SERVER_DATA, NPCActionManager.DIRECTORY);
        return id;
    }

    private void verifyData(NPCData data) {
        if (data.look() != null) {
            for (NPCData.NPCLookId look : data.look()) {
                if (!this.verifier.exists(look.id(), PackType.SERVER_DATA, NPCLookManager.DIRECTORY))
                    throw new IllegalStateException("No look registered for " + look.id());
            }
        }
        if (data.combatActions() != null) {
            for (ResourceLocation action : data.combatActions()) {
                if (!this.verifier.exists(action, PackType.SERVER_DATA, NPCActionManager.DIRECTORY))
                    throw new IllegalStateException("No npc action registered for " + action);
            }
        }
        data.giftItems().forEach((s, g) -> {
            if (g.giftID() != null && !this.verifier.exists(g.giftID(), PackType.SERVER_DATA, GiftManager.DIRECTORY))
                throw new IllegalStateException("No gift registered for " + g.giftID());
        });
    }

    @Override
    public Map<String, String> translations() {
        return this.translations;
    }

    public record QuestResponseBuilder(List<ConversationSet.Builder> start,
                                       List<ConversationSet.Builder> active,
                                       ConversationSet.Builder end) {

        public QuestResponseBuilder(ConversationSet.Builder start, ConversationSet.Builder active,
                                    ConversationSet.Builder end) {
            this(List.of(start), List.of(active), end);
        }
    }
}
