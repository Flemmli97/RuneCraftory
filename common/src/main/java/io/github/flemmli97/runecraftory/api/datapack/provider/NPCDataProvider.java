package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.api.datapack.npc.GiftData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.GiftManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCActionManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.NPCAttackActions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class NPCDataProvider implements DataProvider, AdditionalLanguages {

    private final Map<ResourceLocation, NPCLook> looks = new HashMap<>();
    private final Map<ResourceLocation, ConversationSet> conversations = new HashMap<>();
    private final Map<ResourceLocation, GiftData> giftData = new LinkedHashMap<>();
    private final Map<ResourceLocation, NPCAttackActions> actions = new HashMap<>();
    private final Map<ResourceLocation, NPCData> data = new HashMap<>();

    //Translation for lang
    public final Map<String, Map<String, String>> dialogueTranslations = new LinkedHashMap<>();
    private final Map<String, String> translations = new LinkedHashMap<>();

    private final PackOutput packOutput;
    private final FileVerifier verifier;
    protected final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    public NPCDataProvider(PackOutput packOutput, FileVerifier verifier, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this.packOutput = packOutput;
        this.verifier = verifier;
        this.modid = modid;
        this.provider = provider;
    }

    protected abstract void add(HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenApply(provider -> {
            this.add(provider);
            return provider;
        }).thenCompose(provider -> {
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            this.data.forEach((res, val) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + NPCDataManager.DIRECTORY + "/" + res.getPath() + ".json");
                this.verifyData(val);
                JsonElement obj = NPCData.CODEC.encodeStart(ops, val)
                        .getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            this.looks.forEach((res, val) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + NPCLookManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = NPCLook.CODEC.encodeStart(ops, val).getOrThrow();
                DataProvider.saveStable(cache, obj, path);
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            this.conversations.forEach((res, val) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + NPCConversationManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = ConversationSet.CODEC.encodeStart(ops, val).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            this.giftData.forEach((res, val) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + GiftManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = GiftData.CODEC.encodeStart(ops, val).getOrThrow();
                DataProvider.saveStable(cache, obj, path);
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            this.actions.forEach((res, val) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + NPCActionManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = NPCAttackActions.CODEC.encodeStart(ops, val).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "NPCData for " + this.modid;
    }

    public void addNPCData(String id, NPCData.Builder data, Map<ConversationContext, ConversationSet.Builder> conversations,
                           Map<ResourceLocation, QuestResponseBuilder> questConversations) {
        conversations.forEach((key, value) -> {
            ResourceLocation conversationId = ResourceLocation.fromNamespaceAndPath(this.modid, id + "/" + key.key().getPath());
            this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                    .putAll(value.getTranslations());
            this.conversations.put(conversationId, value.build());
            data.addInteractionIfAbsent(key, conversationId);
        });
        questConversations.forEach((key, value) -> {
            ResourceLocation startId = ResourceLocation.fromNamespaceAndPath(this.modid, id + "/quest_start_" + key.getPath());
            for (int i = 0; i < value.start.size(); i++) {
                String path = startId.getPath();
                if (i != 0)
                    path += "_" + i;
                ResourceLocation runIdI = ResourceLocation.fromNamespaceAndPath(this.modid, path);
                this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                        .putAll(value.start.get(i).getTranslations());
                this.conversations.put(runIdI, value.start.get(i).build());
            }
            ResourceLocation runId = ResourceLocation.fromNamespaceAndPath(this.modid, id + "/quest_active_" + key.getPath());
            for (int i = 0; i < value.active.size(); i++) {
                String path = runId.getPath();
                if (i != 0)
                    path += "_" + i;
                ResourceLocation runIdI = ResourceLocation.fromNamespaceAndPath(this.modid, path);
                this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                        .putAll(value.active.get(i).getTranslations());
                this.conversations.put(runIdI, value.active.get(i).build());
            }
            ResourceLocation endId = ResourceLocation.fromNamespaceAndPath(this.modid, id + "/quest_end_" + key.getPath());
            this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                    .putAll(value.end.getTranslations());
            this.conversations.put(endId, value.end.build());
            data.addQuestResponse(key, startId, runId, endId);
        });
        this.dialogueTranslations.computeIfAbsent(id, o -> new LinkedHashMap<>())
                .putAll(data.getTranslations());
        this.data.put(ResourceLocation.fromNamespaceAndPath(this.modid, id), data.build());
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
