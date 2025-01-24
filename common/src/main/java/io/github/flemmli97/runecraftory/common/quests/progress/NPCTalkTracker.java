package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalk;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class NPCTalkTracker extends ProgressionTrackerBase<EntityNPCBase, NPCTalk> {

    public static final ProgressionTrackerKey<EntityNPCBase, NPCTalk> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "talking_tracker",
            NPCTalk.ID);

    public NPCTalkTracker(NPCTalk questEntry) {
        super(questEntry);
    }

    @Override
    public boolean progress(ServerPlayer player, QuestProgress prog, EntityNPCBase with) {
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
