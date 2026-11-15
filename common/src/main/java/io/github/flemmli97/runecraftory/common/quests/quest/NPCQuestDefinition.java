package io.github.flemmli97.runecraftory.common.quests.quest;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.QuestboardBlock;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.quests.QuestData;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCBasedTask;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestCategory;
import io.github.flemmli97.simplequests_api.quest.QuestDefinition;
import io.github.flemmli97.simplequests_api.quest.QuestDefinitionImpl;
import io.github.flemmli97.simplequests_api.quest.QuestDefinitionRef;
import io.github.flemmli97.simplequests_api.quest.QuestHolder;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.util.TimeDuration;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A quest linked with a npc entity.
 */
public class NPCQuestDefinition extends QuestDefinitionImpl {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_quest");

    public static final MapCodec<NPCQuestDefinition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BaseData.CODEC.forGetter(q -> q.get(true)),
            ResourceLocation.CODEC.listOf().fieldOf("quest_givers").forGetter(d -> d.questGiverNPCDataIDs),
            QuestDialogueEntry.CODEC.listOf().fieldOf("quests").forGetter(d -> d.questDialogues),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(d -> d.loot.location()),
            Codec.BOOL.optionalFieldOf("global").forGetter(d -> Optional.of(d.global)),
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> Optional.of(d.conditions))
    ).apply(instance, (data, questGivers, quests, loot, global, conditions) -> {
        NPCQuestDefinition.Builder builder = data.applyTo(name -> new NPCQuestDefinition.Builder(name, questGivers, loot));
        quests.forEach(builder::withQuests);
        if (global.orElse(false)) {
            builder.global();
        }
        conditions.ifPresent(list -> list.forEach(builder::condition));
        return builder.build();
    }));

    public static final MapCodec<NPCQuestDefinition> OPT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BaseData.CODEC.forGetter(q -> q.get(false)),
            ResourceLocation.CODEC.listOf().fieldOf("quest_givers").forGetter(d -> d.questGiverNPCDataIDs),
            QuestDialogueEntry.OPT_CODEC.listOf().fieldOf("quests").forGetter(d -> d.questDialogues),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(d -> d.loot.location()),
            Codec.BOOL.optionalFieldOf("global").forGetter(d -> d.global ? Optional.of(true) : Optional.empty()),
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.isEmpty() ? Optional.empty() : Optional.of(d.conditions))
    ).apply(instance, (data, questGivers, quests, loot, global, conditions) -> {
        NPCQuestDefinition.Builder builder = data.applyTo(name -> new NPCQuestDefinition.Builder(name, questGivers, loot));
        quests.forEach(builder::withQuests);
        if (global.orElse(false)) {
            builder.global();
        }
        conditions.ifPresent(list -> list.forEach(builder::condition));
        return builder.build();
    }));

    private final List<ResourceLocation> questGiverNPCDataIDs;
    private final List<QuestDialogueEntry> questDialogues;
    private final List<QuestHolder> quests;
    private final List<QuestResponses> dialogues;
    private final ResourceKey<LootTable> loot;
    private final List<LootItemCondition> conditions;
    private final boolean global;

    private NPCQuestDefinition(QuestCategory category, String name, List<String> description,
                               List<ResourceLocation> requiredParentQuests, boolean redoParent,
                               TimeDuration repeatDelay, int repeatDaily, int maxRepeat, boolean isDailyQuest,
                               boolean needsUnlock, EntityPredicate unlockCondition, int sortingId, ItemStack icon, Visibility visibility,
                               List<ResourceLocation> questGiverNPCDataIDs, List<QuestDialogueEntry> quests,
                               ResourceKey<LootTable> loot, List<LootItemCondition> conditions, boolean global) {
        super(category, name, description, requiredParentQuests, redoParent, repeatDelay, repeatDaily, maxRepeat, isDailyQuest, needsUnlock, unlockCondition, sortingId, icon, visibility);
        this.questGiverNPCDataIDs = questGiverNPCDataIDs;
        this.questDialogues = quests;
        this.quests = quests.stream().map(QuestDialogueEntry::quest).toList();
        this.dialogues = quests.stream().map(QuestDialogueEntry::dialogues).toList();
        this.loot = loot;
        this.conditions = conditions;
        this.global = global;
    }

    private static AABB aabbOf(Vec3 pos) {
        return new AABB(pos.add(-QuestboardBlock.RANGE, -QuestboardBlock.RANGE, -QuestboardBlock.RANGE),
                pos.add(QuestboardBlock.RANGE, QuestboardBlock.RANGE, QuestboardBlock.RANGE));
    }

    public static List<NPCQuestSetupData> resolve(NPCQuestDefinition quest, ServerPlayer player, Vec3 at) {
        List<NPCEntity> nearbyNPCs = player.level().getEntities(EntityTypeTest.forClass(NPCEntity.class), aabbOf(at), e -> true);
        return nearbyNPCs.stream().map(npc -> {
            if (!quest.questGiverNPCDataIDs().contains(npc.getDataID()) || npc.isStranger(player) || !quest.matches(player, npc))
                return null;
            Map<ResourceLocation, List<UUID>> relevantNPCs = quest.collectNPCReferences(nearbyNPCs, npc);
            if (relevantNPCs == null)
                return null;
            return new NPCQuestSetupData(quest, npc, relevantNPCs);
        }).filter(Objects::nonNull).toList();
    }

    public static ResourceLocation withUuid(NPCQuestDefinition quest, UUID uuid) {
        if (quest.global())
            return quest.id();
        return ResourceLocation.fromNamespaceAndPath(quest.id().getNamespace(), quest.id().getPath() + "/" + uuid);
    }

    private Map<ResourceLocation, List<UUID>> collectNPCReferences(List<NPCEntity> nearbyNPCs, NPCEntity except) {
        Object2IntMap<ResourceLocation> npcReferences = new Object2IntOpenHashMap<>();
        this.visit(def -> {
            Object2IntMap<ResourceLocation> definitionRefs = new Object2IntOpenHashMap<>();
            for (QuestTask<?> task : def.unresolvedTasks().values()) {
                if (task instanceof NPCBasedTask talk && talk.targetNPCId() != null) {
                    definitionRefs.computeInt(talk.targetNPCId(), (k, i) -> i == 0 ? 1 : i + 1);
                }
            }
            definitionRefs.forEach((k, v) -> {
                int current = npcReferences.getInt(k);
                if (v > current) {
                    npcReferences.put(k, v.intValue());
                }
            });
        });
        if (npcReferences.isEmpty())
            return Map.of();
        Map<ResourceLocation, List<UUID>> relevantNpcs = new HashMap<>();
        for (NPCEntity entity : nearbyNPCs) {
            if (entity == except)
                continue;
            ResourceLocation targetId = entity.getDataID();
            int required = npcReferences.getInt(targetId);
            if (required == 0) {
                targetId = NPCBasedTask.ANY_NPC;
                required = npcReferences.getInt(targetId);
            }
            int current = relevantNpcs.getOrDefault(targetId, List.of()).size();
            if (current < required) {
                relevantNpcs.computeIfAbsent(targetId, k -> new ArrayList<>())
                        .add(entity.getUUID());
            }
        }
        if (relevantNpcs.size() != npcReferences.size()) {
            return null;
        }
        for (Object2IntMap.Entry<ResourceLocation> entry : npcReferences.object2IntEntrySet()) {
            int current = relevantNpcs.getOrDefault(entry.getKey(), List.of()).size();
            if (current < entry.getIntValue()) {
                return null;
            }
        }
        return relevantNpcs;
    }

    public List<ResourceLocation> questGiverNPCDataIDs() {
        return this.questGiverNPCDataIDs;
    }

    public List<QuestResponses> dialogues() {
        return this.dialogues;
    }

    public ResourceKey<LootTable> loot() {
        return this.loot;
    }

    public boolean global() {
        return this.global;
    }

    public boolean matches(ServerPlayer player, NPCEntity npc) {
        if (this.conditions.isEmpty())
            return true;
        LootParams ctx = new LootParams.Builder((ServerLevel) npc.level())
                .withParameter(LootContextParams.THIS_ENTITY, npc)
                .withParameter(LootContextParams.ORIGIN, npc.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withLuck(player.getLuck()).create(LootCtxParameters.NPC_INTERACTION);
        LootContext lootContext = new LootContext.Builder(ctx).create(Optional.empty());
        return this.conditions.stream().allMatch(condition -> condition.test(lootContext));
    }

    @Override
    public MapCodec<? extends QuestDefinition> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation typeId() {
        return ID;
    }

    @Override
    public MapCodec<? extends QuestDefinition> simplifiedCodec() {
        return OPT_CODEC;
    }

    @Override
    public List<QuestHolder> getSubQuests() {
        return this.quests;
    }

    @Override
    public boolean advanceable() {
        return true;
    }

    @Override
    public ResolvedQuest resolve(QuestProgress progress) {
        NPCQuestSetupData data = progress.data() instanceof QuestData d ? d.getSetupDataFor(this) : null;
        if (data == null) {
            return null;
        }
        return new NPCQuest(data.questGiver(), data.relevantNpcs(), this);
    }

    @Override
    public ResolvedQuest load(QuestProgress progress, Tag tag, HolderLookup.Provider provider) {
        return new NPCQuest(this, tag);
    }

    public static class NPCQuest extends QuestDefinitionRef<NPCQuestDefinition> {

        private final UUID questGiverUUID;
        private WeakReference<NPCEntity> questGiver;

        private final ResourceLocation questId;
        private final Map<ResourceLocation, List<UUID>> relevantNPCs;
        private final Set<UUID> relevantNPCIds;

        private final Set<UUID> takenNPCIds = new HashSet<>();

        public NPCQuest(NPCEntity questGiver, Map<ResourceLocation, List<UUID>> relevantNPCs, NPCQuestDefinition definition) {
            super(definition);
            this.questGiverUUID = questGiver.getUUID();
            this.questGiver = new WeakReference<>(questGiver);
            this.relevantNPCs = ImmutableMap.copyOf(relevantNPCs);
            this.relevantNPCIds = this.relevantNPCs.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());
            this.questId = withUuid(definition, this.questGiverUUID);
        }

        public NPCQuest(NPCQuestDefinition definition, Tag t) {
            super(definition);
            CompoundTag tag = (CompoundTag) t;
            this.questGiverUUID = UUIDUtil.CODEC.parse(NbtOps.INSTANCE, tag.get("QuestGiver")).getOrThrow();
            ImmutableMap.Builder<ResourceLocation, List<UUID>> builder = ImmutableMap.builder();
            CompoundTag relevantNPCs = tag.getCompound("RelevantNPCs");
            relevantNPCs.getAllKeys().forEach(id -> {
                builder.put(ResourceLocation.parse(id), UUIDUtil.CODEC.listOf().parse(NbtOps.INSTANCE, relevantNPCs.get(id)).getOrThrow());
            });
            this.relevantNPCs = builder.build();
            this.relevantNPCIds = this.relevantNPCs.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());
            ListTag taken = tag.getList("TakenNPCs", Tag.TAG_INT_ARRAY);
            taken.forEach(val -> this.takenNPCIds.add(UUIDUtil.CODEC.parse(NbtOps.INSTANCE, val).getOrThrow()));
            this.questId = withUuid(definition, this.questGiverUUID);
        }

        public UUID getQuestGiverUUID() {
            return this.questGiverUUID;
        }

        public ResourceLocation getOriginId() {
            return super.id();
        }

        public UUID getRandomNpcFor(RandomSource random, ResourceLocation npcId) {
            List<UUID> uuids = this.relevantNPCs.get(npcId);
            if (uuids == null || uuids.isEmpty()) {
                return null;
            }
            uuids = uuids.stream().filter(id -> !this.takenNPCIds.contains(id)).toList();
            if (uuids.isEmpty()) {
                return null;
            }
            UUID randomId = uuids.get(random.nextInt(uuids.size()));
            this.takenNPCIds.add(randomId);
            return randomId;
        }

        public Set<UUID> relevantNpcIds() {
            return this.relevantNPCIds;
        }

        public boolean isValid(MinecraftServer server) {
            RunecraftorySavedData savedData = RunecraftorySavedData.get(server);
            if (!savedData.npcHandler.doesNPCExist(this.getQuestGiverUUID())) {
                return false;
            }
            return this.relevantNPCIds.stream().allMatch(savedData.npcHandler::doesNPCExist);
        }

        @Nullable
        public NPCEntity getNpc(Level level) {
            NPCEntity npc = this.questGiver == null ? null : this.questGiver.get();
            if (npc == null || !npc.isAlive()) {
                npc = EntityUtils.findFromUUID(NPCEntity.class, level, this.getQuestGiverUUID());
                this.questGiver = new WeakReference<>(npc);
            }
            return npc;
        }

        @Override
        public ResourceLocation id() {
            return this.questId;
        }

        @Override
        public List<MutableComponent> description(ServerPlayer player) {
            NPCEntity npc = this.getNpc(player.level());
            if (npc != null) {
                return super.description(player).stream().map(s -> Component.translatable(s.getString(), npc.getCustomName(), npc.getX(), npc.getY(), npc.getZ())).collect(Collectors.toList());
            }
            return super.description(player);
        }

        @Override
        public String submissionTrigger(ServerPlayer player) {
            return this.getQuestGiverUUID().toString();
        }

        @Override
        public void onComplete(ServerPlayer serverPlayer) {
            TreasureChestEntity chest = RuneCraftoryEntities.TREASURE_CHEST.get().create(serverPlayer.serverLevel());
            if (chest != null && this.source().loot != null && !this.source().loot.equals(BuiltInLootTables.EMPTY)) {
                chest.absMoveTo(serverPlayer.getX(2), serverPlayer.getY(1.5), serverPlayer.getZ(2), serverPlayer.getRandom().nextFloat() * 360.0f, 0.0f);
                int tries = 0;
                while (!serverPlayer.level().noCollision(chest) && tries < 10) {
                    chest.absMoveTo(serverPlayer.getX(2), serverPlayer.getY(1.5), serverPlayer.getZ(2), serverPlayer.getRandom().nextFloat() * 360.0f, 0.0f);
                    tries++;
                }
                chest.setChestLoot(this.source().loot);
                serverPlayer.serverLevel().addFreshEntity(chest);
            }
            if (this.getNpc(serverPlayer.level()) != null)
                this.getNpc(serverPlayer.level()).completeNPCQuest(serverPlayer, this);
            this.onReset(serverPlayer);
        }

        public void releaseNPC(UUID uuid) {
            this.takenNPCIds.remove(uuid);
        }

        @Override
        public Tag save(HolderLookup.Provider lookup) {
            CompoundTag tag = new CompoundTag();
            tag.put("QuestGiver", UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, this.questGiverUUID).getOrThrow());
            CompoundTag relevantNPCs = new CompoundTag();
            this.relevantNPCs.forEach((id, uuid) -> relevantNPCs.put(id.toString(), UUIDUtil.CODEC.listOf().encodeStart(NbtOps.INSTANCE, uuid).getOrThrow()));
            tag.put("RelevantNPCs", relevantNPCs);
            ListTag taken = new ListTag();
            this.takenNPCIds.forEach(id -> taken.add(UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, id).getOrThrow()));
            tag.put("TakenNPCs", taken);
            return tag;
        }
    }

    public static class Builder extends BuilderBase<NPCQuestDefinition, NPCQuestDefinition.Builder> {

        private final List<ResourceLocation> questGiverNPCDataIDs;
        private final List<QuestDialogueEntry> quests = new ArrayList<>();
        private final ResourceKey<LootTable> loot;
        private final List<LootItemCondition> conditions = new ArrayList<>();
        private boolean global;

        public Builder(String task, ResourceLocation npcDataID, ResourceLocation loot) {
            this(task, List.of(npcDataID), loot);
        }

        public Builder(String task, List<ResourceLocation> questGiverNPCDataIDs, ResourceLocation loot) {
            super(task);
            this.questGiverNPCDataIDs = questGiverNPCDataIDs;
            this.loot = ResourceKey.create(Registries.LOOT_TABLE, loot);
        }

        public final Builder withQuests(QuestDialogueEntry... quest) {
            this.quests.addAll(List.of(quest));
            return this;
        }

        public Builder withQuests(Collection<QuestDialogueEntry> quest) {
            this.quests.addAll(quest);
            return this;
        }

        public Builder condition(LootItemCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public Builder global() {
            this.global = true;
            return this;
        }

        @Override
        protected Builder asThis() {
            return this;
        }

        @Override
        public NPCQuestDefinition build() {
            if (this.quests.isEmpty())
                throw new IllegalStateException("Quests not defined");
            return new NPCQuestDefinition(this.category, this.name, this.description,
                    this.requiredParentQuests, this.redoParent,
                    this.repeatDelay, this.repeatDaily, this.maxRepeat,
                    this.isDailyQuest, this.needsUnlock, this.unlockCondition, this.sortingId, this.icon, this.visibility,
                    this.questGiverNPCDataIDs, this.quests, this.loot, this.conditions, this.global);
        }
    }

}
