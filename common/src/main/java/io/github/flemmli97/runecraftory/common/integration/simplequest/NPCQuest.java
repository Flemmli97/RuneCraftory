package io.github.flemmli97.runecraftory.common.integration.simplequest;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.simplequests.api.QuestEntry;
import io.github.flemmli97.simplequests.datapack.QuestsManager;
import io.github.flemmli97.simplequests.quest.QuestCategory;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCQuest extends QuestBase {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "npc_quest");

    private EntityNPCBase npc;
    private UUID npcUuid;
    public final List<ResourceLocation> npcDataIDs;
    public final ResourceLocation quest, loot;
    public final List<ResourceLocation> parentQuests;
    private ResourceLocation originID;

    protected NPCQuest(ResourceLocation id, QuestCategory category, String questTaskString, List<String> questTaskDesc,
                       List<ResourceLocation> parents, boolean redoParent, int repeatDelay, int sortingId, EntityPredicate unlockCondition, List<ResourceLocation> npcDataIDs, ResourceLocation quest, ResourceLocation loot) {
        super(id, category, questTaskString, questTaskDesc,
                List.of(), redoParent, false, ItemStack.EMPTY, repeatDelay, 0, sortingId, false, unlockCondition);
        this.npcDataIDs = npcDataIDs;
        this.parentQuests = parents;
        this.quest = quest;
        this.loot = loot;
        this.originID = this.id;
    }

    public static ResourceLocation withUuid(ResourceLocation original, UUID uuid) {
        if (uuid == null)
            return original;
        return new ResourceLocation(original.getNamespace(), original.getPath() + "/" + uuid);
    }

    public static NPCQuest of(ResourceLocation id, EntityNPCBase npc, QuestBase quest) {
        return QuestBase.of(task -> new Builder(id, task, List.of(npc.getDataID()),
                quest.getLoot()).withQuest(quest.id), quest.category, quest.serialize(true, false)).build();
    }

    public static NPCQuest of(ResourceLocation id, QuestCategory category, JsonObject obj) {
        UUID uuid = obj.has("npc_uuid") ? UUID.fromString(obj.get("npc_uuid").getAsString()) : null;
        List<ResourceLocation> npc_ids = new ArrayList<>();
        try {
            npc_ids.add(new ResourceLocation(GsonHelper.getAsString(obj, "npc_id")));
        } catch (JsonSyntaxException e) {
            JsonArray arr = GsonHelper.getAsJsonArray(obj, "npc_id");
            arr.forEach(element -> npc_ids.add(new ResourceLocation(element.getAsString())));
        }
        NPCQuest quest = QuestBase.of(task -> new Builder(withUuid(id, uuid), task, npc_ids,
                new ResourceLocation(GsonHelper.getAsString(obj, "loot_table")))
                .withQuest(new ResourceLocation(GsonHelper.getAsString(obj, "quest"))), category, obj).build();
        quest.npcUuid = uuid;
        quest.originID = id;
        return quest;
    }

    public static List<NPCQuest> of(NPCQuest quest, ServerPlayer player) {
        return player.level.getEntities(EntityTypeTest.forClass(EntityNPCBase.class), player.getBoundingBox().inflate(48), e -> {
                    if (quest.npcDataIDs.contains(e.getDataID()) && e.canAcceptNPCQuest(player, quest)) {
                        ResourceLocation id = SimpleQuestIntegration.INST().questForExists(player, e);
                        return id == null || quest.getOriginID().equals(id);
                    }
                    return false;
                })
                .stream().map(npc -> {
                    NPCQuest ret = of(withUuid(quest.id, npc.getUUID()), quest.category, quest.serialize(true, false));
                    ret.npc = npc;
                    ret.originID = quest.id;
                    ret.npcUuid = npc.getUUID();
                    return ret;
                }).toList();
    }

    @Override
    public MutableComponent getTask(ServerPlayer player, int idx) {
        return new TranslatableComponent(this.questTaskString);
    }

    @Override
    public List<MutableComponent> getDescription(ServerPlayer player, int idx) {
        if (this.npcUuid != null) {
            EntityNPCBase npc = EntityUtil.findFromUUID(EntityNPCBase.class, player.getLevel(), this.npcUuid);
            if (npc != null)
                return this.questTaskDesc.stream().map(s -> new TranslatableComponent(s, npc.getCustomName(), npc.getX(), npc.getY(), npc.getZ())).collect(Collectors.toList());
        }
        return super.getDescription(player, idx);
    }

    @Override
    public JsonObject serialize(boolean withId, boolean full) {
        JsonObject obj = super.serialize(withId, full);
        if (!this.parentQuests.isEmpty() || full) {
            if (this.parentQuests.size() == 1) {
                obj.addProperty("parent_id", this.parentQuests.get(0).toString());
            } else {
                JsonArray arr = new JsonArray();
                this.parentQuests.forEach((r) -> arr.add(r.toString()));
                obj.add("parent_id", arr);
            }
        }
        if (this.npcDataIDs.size() == 1)
            obj.addProperty("npc_id", this.npcDataIDs.get(0).toString());
        else {
            JsonArray arr = new JsonArray();
            this.npcDataIDs.forEach(res -> arr.add(res.toString()));
            obj.add("npc_id", arr);
        }
        obj.addProperty("quest", this.quest.toString());
        obj.addProperty("loot_table", this.loot.toString());
        if (this.npcUuid != null)
            obj.addProperty("npc_uuid", this.npcUuid.toString());
        if (withId) {
            obj.addProperty("id", this.originID.toString());
        }
        obj.addProperty(QuestBase.TYPE_ID, ID.toString());
        return obj;
    }

    public UUID getNpcUuid() {
        return this.npcUuid;
    }

    @Nullable
    public EntityNPCBase getNpc() {
        return this.npc;
    }

    public ResourceLocation getOriginID() {
        return this.originID;
    }

    @Override
    public String submissionTrigger(ServerPlayer player, int idx) {
        if (this.npcUuid == null)
            return super.submissionTrigger(player, idx);
        return this.npcUuid.toString();
    }

    @Override
    public QuestBase resolveToQuest(ServerPlayer player, int idx) {
        QuestBase quest = QuestsManager.instance().getAllQuests().get(this.quest);
        if (quest == null)
            return null;
        return quest.resolveToQuest(player, idx);
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
        this.onReset(serverPlayer);
    }

    @Override
    public void onReset(ServerPlayer player) {
        if (this.npcUuid != null) {
            EntityNPCBase npc = EntityUtil.findFromUUID(EntityNPCBase.class, player.level, this.npcUuid);
            if (npc != null)
                npc.resetQuestProcess(player, this.originID);
            else {
                WorldHandler.get(player.getServer()).npcHandler.scheduleQuestTrackerReset(this.npcUuid, player.getUUID(), this.originID);
            }
        }
    }

    @Override
    public Map<String, QuestEntry> resolveTasks(ServerPlayer player, int idx) {
        QuestBase base = QuestsManager.instance().getAllQuests().get(this.quest);
        Map<String, QuestEntry> result = new HashMap<>(base.resolveTasks(player, idx));
        List<Map.Entry<String, QuestEntry>> talks = result.entrySet().stream().filter(e -> e.getValue() instanceof QuestTasks.NPCTalk).toList();
        talks.forEach(e -> result.put(e.getKey(), e.getValue().resolve(player, this)));
        return ImmutableMap.copyOf(result);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Builder extends BuilderBase<NPCQuest.Builder> {

        private final List<ResourceLocation> npcDataID;
        private ResourceLocation quest;
        private final ResourceLocation loot;

        public Builder(ResourceLocation id, String task, ResourceLocation npcDataID, ResourceLocation loot) {
            this(id, task, List.of(npcDataID), loot);
        }

        public Builder(ResourceLocation id, String task, List<ResourceLocation> npcDataID, ResourceLocation loot) {
            super(id, task);
            this.npcDataID = npcDataID;
            this.loot = loot;
        }

        public Builder withQuest(ResourceLocation quest) {
            this.quest = quest;
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
            if (this.quest == null)
                throw new IllegalStateException("Quest is not defined");
            return new NPCQuest(this.id, this.category, this.questTaskString, this.questDesc, this.neededParentQuests, this.redoParent, this.repeatDelay, this.sortingId,
                    this.unlockCondition, this.npcDataID, this.quest, this.loot);
        }
    }
}
