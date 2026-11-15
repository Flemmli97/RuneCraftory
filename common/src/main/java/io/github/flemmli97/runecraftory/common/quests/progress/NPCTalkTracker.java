package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.player.TaskResult;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class NPCTalkTracker extends ProgressionTrackerBase<NPCEntity, NPCTalkTask.NPCTalkResolved> {

    public static final ProgressionTrackerKey<NPCEntity, NPCTalkTask.NPCTalkResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "talking_tracker",
            NPCTalkTask.ID);

    private boolean gaveLoot;

    public NPCTalkTracker(NPCTalkTask.NPCTalkResolved questEntry) {
        super(questEntry);
    }

    @Override
    public TaskResult progress(ServerPlayer player, QuestProgress prog, NPCEntity with) {
        if (this.questEntry().trySubmit(player, with)) {
            if (!this.gaveLoot) {
                this.gaveLoot = true;
                if (prog.getRootQuest() instanceof NPCQuestDefinition.NPCQuest quest)
                    quest.releaseNPC(this.questEntry().targetNPC());
                TreasureChestEntity chest = RuneCraftoryEntities.TREASURE_CHEST.get().create(player.serverLevel());
                if (chest != null && this.questEntry().lootReturn() != null && !this.questEntry().lootReturn().equals(BuiltInLootTables.EMPTY)) {
                    chest.absMoveTo(player.getX(2), player.getY(1.5), player.getZ(2), player.getRandom().nextFloat() * 360.0f, 0.0f);
                    int tries = 0;
                    while (!player.level().noCollision(chest) && tries < 10) {
                        chest.absMoveTo(player.getX(2), player.getY(1.5), player.getZ(2), player.getRandom().nextFloat() * 360.0f, 0.0f);
                        tries++;
                    }
                    chest.setChestLoot(this.questEntry().lootReturn());
                    player.serverLevel().addFreshEntity(chest);
                }
            }
            return TaskResult.COMPLETED;
        }
        return TaskResult.NONE;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        return this.questEntry().translation(player);
    }

    @Override
    public Tag save() {
        return ByteTag.valueOf(this.gaveLoot);
    }

    @Override
    public void load(Tag tag) {
        this.gaveLoot = ((ByteTag) tag).getAsByte() != 0;
    }
}
