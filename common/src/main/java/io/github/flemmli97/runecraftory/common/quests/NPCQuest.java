package io.github.flemmli97.runecraftory.common.quests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.QuestCategory;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.simplequests_api.registry.QuestBaseRegistry;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A quest linked with a npc entity.
 */
public class NPCQuest extends QuestBase {

    public static final ResourceLocation ID = RuneCraftory.modRes("npc_quest");

    public static final Function<QuestBaseRegistry.CodecContext, MapCodec<NPCQuest>> CODEC = Util.memoize(ctx ->
            QuestBase.buildCodec(NPCQuestData.CODEC
                    .forGetter(q -> new NPCQuest.NPCQuestData(q.npcDataIDs,
                            q.quests, q.loot, q.global, q.dynamicData)), ctx, (id, task, data) -> {
                NPCQuest.Builder builder = new NPCQuest.Builder(id, task, data.npcIDs, data.loot);
                if (data.global)
                    builder.global();
                builder.withQuests(data.quests);
                if (data.dynamic != null)
                    builder.withData(data.dynamic());
                return builder;
            }));

    private EntityNPCBase npc;
    private DynamicQuestData dynamicData;

    public final List<ResourceLocation> npcDataIDs;
    public final List<ResourceLocation> quests;
    public final ResourceLocation loot;
    public final boolean global;

    private NPCQuest(ResourceLocation id, QuestCategory category, String questTaskString, List<String> questTaskDesc,
                     List<ResourceLocation> parents, boolean redoParent, int repeatDelay, int maxRepeat, int sortingId, EntityPredicate unlockCondition, List<ResourceLocation> npcDataIDs, List<ResourceLocation> quests, ResourceLocation loot, boolean global) {
        super(id, category, questTaskString, questTaskDesc,
                parents, redoParent, false, ItemStack.EMPTY, repeatDelay, 0, maxRepeat, sortingId, false, unlockCondition, Visibility.NEVER);
        this.npcDataIDs = npcDataIDs;
        this.quests = quests;
        this.loot = loot;
        this.global = global;
    }

    private static ResourceLocation withUuid(ResourceLocation original, UUID uuid) {
        if (uuid == null)
            return original;
        return new ResourceLocation(original.getNamespace(), original.getPath() + "/" + uuid);
    }

    private static AABB aabbOf(Vec3 pos) {
        return new AABB(pos.add(-BlockQuestboard.RANGE, -BlockQuestboard.RANGE, -BlockQuestboard.RANGE),
                pos.add(BlockQuestboard.RANGE, BlockQuestboard.RANGE, BlockQuestboard.RANGE));
    }

    public static List<NPCQuest> resolve(NPCQuest quest, ServerPlayer player, Vec3 at) {
        return player.level.getEntities(EntityTypeTest.forClass(EntityNPCBase.class), aabbOf(at), e -> {
                    if (quest.npcDataIDs.contains(e.getDataID()) && e.canAcceptNPCQuest(player, quest)) {
                        ResourceLocation id = QuestHandler.questForExists(player, e);
                        return id == null || quest.getOriginID().equals(id);
                    }
                    return false;
                })
                .stream().map(quest::forNPC).toList();
    }

    private NPCQuest forNPC(EntityNPCBase npc) {
        ResourceLocation newID = withUuid(this.id, npc.getUUID());
        NPCQuest quest = new NPCQuest(newID, this.category, this.name, this.description,
                this.npcDataIDs, this.redoParent, this.repeatDelay, this.maxRepeat, this.sortingId,
                this.unlockCondition, this.npcDataIDs, this.quests, this.loot, this.global);
        quest.withNPC(npc, this.id);
        return quest;
    }

    private void withNPC(EntityNPCBase npc, ResourceLocation originID) {
        this.npc = npc;
        this.withNPC(npc.getUUID(), originID);
    }

    private void withNPC(UUID npc, ResourceLocation originID) {
        this.dynamicData = new DynamicQuestData(npc, originID);
    }

    @Override
    public ResourceLocation getTypeId() {
        return ID;
    }

    @Override
    public boolean isUnlocked(ServerPlayer player) {
        return super.isUnlocked(player) && (this.getNpc(player.level) == null || this.getNpc(player.level).canAcceptNPCQuest(player, this));
    }

    @Override
    public MutableComponent getName(ServerPlayer player, int idx) {
        return Component.translatable(this.name);
    }

    @Override
    public List<MutableComponent> getDescription(ServerPlayer player, int idx) {
        EntityNPCBase npc = this.getNpc(player.level);
        if (npc != null) {
            return this.description.stream().map(s -> Component.translatable(s, npc.getCustomName(), npc.getX(), npc.getY(), npc.getZ())).collect(Collectors.toList());
        }
        return super.getDescription(player, idx);
    }

    public UUID getNpcUuid() {
        return this.dynamicData != null ? this.dynamicData.npcUuid() : null;
    }

    @Nullable
    public EntityNPCBase getNpc(Level level) {
        if (this.dynamicData != null && this.npc == null)
            this.npc = EntityUtil.findFromUUID(EntityNPCBase.class, level, this.dynamicData.npcUuid());
        return this.npc;
    }

    public ResourceLocation getOriginID() {
        return this.dynamicData != null ? this.dynamicData.origin() : this.id;
    }

    @Override
    public String submissionTrigger(ServerPlayer player, int idx) {
        if (this.dynamicData == null)
            return super.submissionTrigger(player, idx);
        return this.dynamicData.npcUuid().toString();
    }

    @Override
    public QuestBase resolveToQuest(ServerPlayer player, int idx) {
        if (idx < 0 || idx >= this.quests.size())
            return null;
        ResourceLocation id = this.quests.get(idx);
        QuestBase quest = QuestsManager.instance().getQuest(id);
        if (quest == null)
            return null;
        return quest.resolveToQuest(player, 0);
    }

    @Override
    public ResourceLocation getLoot() {
        return this.loot;
    }

    @Override
    public void onComplete(ServerPlayer serverPlayer) {
        EntityTreasureChest chest = ModEntities.TREASURE_CHEST.get().create(serverPlayer.getLevel());
        if (chest != null && this.getLoot() != null && !this.getLoot().equals(BuiltInLootTables.EMPTY)) {
            chest.absMoveTo(serverPlayer.getX(2), serverPlayer.getY(1.5), serverPlayer.getZ(2), serverPlayer.getRandom().nextFloat() * 360.0f, 0.0f);
            int tries = 0;
            while (!serverPlayer.level.noCollision(chest) && tries < 10) {
                chest.absMoveTo(serverPlayer.getX(2), serverPlayer.getY(1.5), serverPlayer.getZ(2), serverPlayer.getRandom().nextFloat() * 360.0f, 0.0f);
                tries++;
            }
            chest.setChestLoot(this.loot);
            serverPlayer.getLevel().addFreshEntity(chest);
        }
        if (this.getNpc(serverPlayer.level) != null)
            this.getNpc(serverPlayer.level).completeNPCQuest(serverPlayer, this);
        this.onReset(serverPlayer);
    }

    @Override
    public void onReset(ServerPlayer player) {
        if (this.dynamicData != null) {
            EntityNPCBase npc = this.getNpc(player.level);
            if (npc != null)
                npc.resetQuestProcess(player, this.dynamicData.origin());
            else {
                WorldHandler.get(player.getServer()).npcHandler.scheduleQuestTrackerReset(this.dynamicData.npcUuid(), player.getUUID(), this.dynamicData.origin());
            }
        }
    }

    @Override
    public Map<String, ResolvedQuestTask> resolveTasks(PlayerQuestData data, QuestProgress progress, int idx) {
        QuestBase base = this.resolveToQuest(data.getPlayer(), idx);
        if (base == null)
            return Map.of();
        return base.resolveTasks(data, progress, 0);
    }

    @Override
    public boolean isDynamic() {
        return this.dynamicData != null;
    }

    public static class Builder extends BuilderBase<NPCQuest, NPCQuest.Builder> {

        private final List<ResourceLocation> npcDataID;
        private final List<ResourceLocation> quests = new ArrayList<>();
        private final ResourceLocation loot;
        private boolean global;
        private DynamicQuestData dynamic;

        public Builder(ResourceLocation id, String task, ResourceLocation npcDataID, ResourceLocation loot) {
            this(id, task, List.of(npcDataID), loot);
        }

        public Builder(ResourceLocation id, String task, List<ResourceLocation> npcDataID, ResourceLocation loot) {
            super(id, task);
            this.npcDataID = npcDataID;
            this.loot = loot;
        }

        public Builder withQuests(ResourceLocation... quest) {
            this.quests.addAll(List.of(quest));
            return this;
        }

        public Builder withQuests(Collection<ResourceLocation> quest) {
            this.quests.addAll(quest);
            return this;
        }

        public Builder global() {
            this.global = true;
            return this;
        }

        private Builder withData(DynamicQuestData dynamic) {
            this.dynamic = dynamic;
            return this;
        }

        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        protected Builder asThis() {
            return this;
        }

        @Override
        public NPCQuest build() {
            if (this.quests.isEmpty())
                throw new IllegalStateException("Quests not defined");
            NPCQuest quest = new NPCQuest(this.id, this.category, this.name, this.description, this.neededParentQuests,
                    this.redoParent, this.repeatDelay, this.maxRepeat, this.sortingId,
                    this.unlockCondition, this.npcDataID, this.quests, this.loot, this.global);
            if (this.dynamic != null)
                quest.withNPC(this.dynamic.npcUuid(), this.dynamic.origin());
            return quest;
        }
    }

    private record NPCQuestData(List<ResourceLocation> npcIDs, List<ResourceLocation> quests, ResourceLocation loot,
                                boolean global, DynamicQuestData dynamic) {

        static final MapCodec<NPCQuestData> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                        Codec.BOOL.optionalFieldOf("global").forGetter(d -> d.global ? Optional.of(true) : Optional.empty()),
                        DynamicQuestData.CODEC.optionalFieldOf("dynamic_data").forGetter(d -> Optional.ofNullable(d.dynamic)),

                        ResourceLocation.CODEC.listOf().fieldOf("target_npc_ids").forGetter(d -> d.npcIDs),
                        ResourceLocation.CODEC.listOf().fieldOf("quests").forGetter(d -> d.quests),
                        ResourceLocation.CODEC.fieldOf("loot_table").forGetter(d -> d.loot)
                ).apply(inst, (global, dynamic, target, quests, loot) -> new NPCQuestData(target, quests, loot, global.orElse(false), dynamic.orElse(null)))
        );
    }

    private record DynamicQuestData(UUID npcUuid, ResourceLocation origin) {

        static final Codec<DynamicQuestData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("npcUuid").forGetter(d -> d.npcUuid),
                ResourceLocation.CODEC.fieldOf("origin_id").forGetter(d -> d.origin)
        ).apply(inst, DynamicQuestData::new));
    }
}
