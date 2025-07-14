package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class NPCTalkTracker extends ProgressionTrackerBase<NPCEntity, NPCTalkTask.NPCTalkResolved> {

    public static final ProgressionTrackerKey<NPCEntity, NPCTalkTask.NPCTalkResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "talking_tracker",
            NPCTalkTask.ID);

    public NPCTalkTracker(NPCTalkTask.NPCTalkResolved questEntry) {
        super(questEntry);
    }

    @Override
    public boolean progress(ServerPlayer player, QuestProgress prog, NPCEntity with) {
        return this.questEntry().trySubmit(player, with);
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        return this.questEntry().translation(player);
    }

    @Override
    public Tag save() {
        return EndTag.INSTANCE;
    }

    @Override
    public void load(Tag tag) {
    }
}
