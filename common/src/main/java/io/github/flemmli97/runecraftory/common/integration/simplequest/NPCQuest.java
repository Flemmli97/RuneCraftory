package io.github.flemmli97.runecraftory.common.integration.simplequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCQuest extends QuestBase {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "npc_quest");

    private UUID npcUuid;
    public final ResourceLocation npcDataID, quest, loot;
    public final List<ResourceLocation> parentQuests;
    private ResourceLocation originID;

    public NPCQuest(ResourceLocation id, QuestCategory category, String questTaskString, List<String> questTaskDesc,
                    List<ResourceLocation> parents, boolean redoParent, int sortingId, EntityPredicate unlockCondition, ResourceLocation npcDataID, ResourceLocation quest, ResourceLocation loot) {
        super(id, category, questTaskString, questTaskDesc,
                List.of(), redoParent, false, ItemStack.EMPTY, 0, 0, sortingId, false, unlockCondition);
        this.npcDataID = npcDataID;
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
        return QuestBase.of(task -> new Builder(id, task, npc.getDataID(),
                quest.getLoot()).withQuest(quest.id), quest.category, quest.serialize(true, false)).build();
    }

    public static NPCQuest of(ResourceLocation id, QuestCategory category, JsonObject obj) {
        UUID uuid = obj.has("npc_uuid") ? UUID.fromString(obj.get("npc_uuid").getAsString()) : null;
        NPCQuest quest = QuestBase.of(task -> new Builder(withUuid(id, uuid), task, new ResourceLocation(GsonHelper.getAsString(obj, "npc_id")),
                new ResourceLocation(GsonHelper.getAsString(obj, "loot_table")))
                .withQuest(new ResourceLocation(GsonHelper.getAsString(obj, "quest"))), category, obj).build();
        quest.npcUuid = uuid;
        quest.originID = id;
        return quest;
    }

    public static List<NPCQuest> of(NPCQuest quest, ServerPlayer player) {
        return player.level.getEntities(EntityTypeTest.forClass(EntityNPCBase.class), player.getBoundingBox().inflate(48), e -> {
                    if (e.getDataID().equals(quest.npcDataID) && e.canAcceptNPCQuest(player, quest)) {
                        ResourceLocation id = SimpleQuestIntegration.INST().questForExists(player, e);
                        return id == null || quest.getOriginID().equals(id);
                    }
                    return false;
                })
                .stream().map(npc -> {
                    NPCQuest ret = of(withUuid(quest.id, npc.getUUID()), quest.category, quest.serialize(true, false));
                    ret.originID = quest.id;
                    ret.npcUuid = npc.getUUID();
                    return ret;
                }).toList();
    }

    @Override
    public List<MutableComponent> getDescription(ServerPlayer player) {
        if (this.npcUuid != null) {
            EntityNPCBase npc = EntityUtil.findFromUUID(EntityNPCBase.class, player.getLevel(), this.npcUuid);
            if (npc != null)
                return this.questTaskDesc.stream().map(s -> new TranslatableComponent(s, npc.getCustomName(), npc.getX(), npc.getY(), npc.getZ())).collect(Collectors.toList());
        }
        return super.getDescription(player);
    }

    @Override
    public JsonObject serialize(boolean withId, boolean full) {
        JsonObject obj = super.serialize(withId, full);
        if (!this.parentQuests.isEmpty() || full) {
            if (this.parentQuests.size() == 1) {
                obj.addProperty("parent_id", this.parentQuests.get(0).toString());
            } else {
                JsonArray arr = new JsonArray();
                this.parentQuests.forEach((r) -> {
                    arr.add(r.toString());
                });
                obj.add("parent_id", arr);
            }
        }
        obj.addProperty("npc_id", this.npcDataID.toString());
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
        if (idx != 0)
            return null;
        return QuestsManager.instance().getAllQuests().get(this.quest);
    }

    @Override
    public ResourceLocation getLoot() {
        return this.loot;
    }

    @Override
    public void onComplete(ServerPlayer serverPlayer) {
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
        return base.resolveTasks(player, 0);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Builder extends BuilderBase<NPCQuest.Builder> {

        private final ResourceLocation npcDataID;
        private ResourceLocation quest;
        private final ResourceLocation loot;

        public Builder(ResourceLocation id, String task, ResourceLocation npcDataID, ResourceLocation loot) {
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
            return new NPCQuest(this.id, this.category, this.questTaskString, this.questDesc, this.neededParentQuests, this.redoParent, this.sortingId,
                    this.unlockCondition, this.npcDataID, this.quest, this.loot);
        }
    }
}
