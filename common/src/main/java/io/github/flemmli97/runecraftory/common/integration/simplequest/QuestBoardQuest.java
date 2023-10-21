package io.github.flemmli97.runecraftory.common.integration.simplequest;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.simplequests.api.QuestEntry;
import io.github.flemmli97.simplequests.datapack.QuestsManager;
import io.github.flemmli97.simplequests.quest.QuestCategory;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.Map;

public class QuestBoardQuest extends QuestBase {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "quest_board_quest");

    private final QuestBase quest;

    public QuestBoardQuest(QuestBase base) {
        super(base.id, base.category, "", List.of(), base.neededParentQuests, base.redoParent, base.needsUnlock,
                base.getIcon(), base.repeatDelay, base.repeatDaily, base.sortingId, base.isDailyQuest, EntityPredicate.ANY);
        this.quest = base;
    }

    public static QuestBoardQuest of(ResourceLocation id, QuestCategory category, JsonObject obj) {
        QuestBase wrapped = QuestsManager.instance().getAllQuests().get(new ResourceLocation(obj.get("WrappedQuest").getAsString()));
        return new QuestBoardQuest(wrapped);
    }

    @Override
    public JsonObject serialize(boolean withId, boolean full) {
        JsonObject obj = new JsonObject();
        if (withId)
            obj.addProperty("id", this.id.toString());
        if (this.category != QuestCategory.DEFAULT_CATEGORY)
            obj.addProperty("category", this.category.id.toString());
        obj.addProperty("WrappedQuest", this.quest.id.toString());
        obj.addProperty(QuestBase.TYPE_ID, ID.toString());
        return obj;
    }

    @Override
    public MutableComponent getTask(ServerPlayer player, int idx) {
        return this.quest.getTask(player, idx);
    }

    @Override
    public List<MutableComponent> getDescription(ServerPlayer player, int idx) {
        return this.quest.getDescription(player, idx);
    }

    @Override
    public String submissionTrigger(ServerPlayer player, int idx) {
        return this.quest.submissionTrigger(player, idx);
    }

    @Override
    public QuestBase resolveToQuest(ServerPlayer player, int idx) {
        return this.quest.resolveToQuest(player, idx);
    }

    @Override
    public ResourceLocation getLoot() {
        return this.quest.getLoot();
    }

    @Override
    public Map<String, QuestEntry> resolveTasks(ServerPlayer player, int questIndex) {
        return this.quest.resolveTasks(player, questIndex);
    }

    @Override
    public boolean isDynamic() {
        return true;
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
            chest.setChestLoot(this.getLoot());
            serverPlayer.getLevel().addFreshEntity(chest);
        }
    }
}
