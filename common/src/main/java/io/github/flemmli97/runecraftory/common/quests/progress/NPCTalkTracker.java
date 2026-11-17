package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.player.TaskResult;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class NPCTalkTracker extends ProgressionTrackerBase<NPCEntity, NPCTalkTask.NPCTalkResolved> {

    public static final ProgressionTrackerKey<NPCEntity, NPCTalkTask.NPCTalkResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "talking_tracker",
            NPCTalkTask.ID);

    private boolean completed;

    public NPCTalkTracker(NPCTalkTask.NPCTalkResolved questEntry) {
        super(questEntry);
    }

    @Override
    public TaskResult progress(ServerPlayer player, QuestProgress prog, NPCEntity with) {
        if (this.questEntry().trySubmit(player, with)) {
            if (!this.completed) {
                if (prog.getRootQuest() instanceof NPCQuestDefinition.NPCQuest quest)
                    quest.releaseNPC(this.questEntry().targetNPC());
                this.completed = true;
                return TaskResult.COMPLETED;
            }
        }
        return TaskResult.NONE;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        return this.questEntry().translation(player);
    }

    @Override
    public Tag save() {
        return ByteTag.valueOf(this.completed);
    }

    @Override
    public void load(Tag tag) {
        this.completed = ((ByteTag) tag).getAsByte() != 0;
    }
}
