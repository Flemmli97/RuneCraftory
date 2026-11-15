package io.github.flemmli97.runecraftory.common.quests.quest;

import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record NPCQuestSetupData(NPCQuestDefinition quest, NPCEntity questGiver,
                                Map<ResourceLocation, List<UUID>> relevantNpcs) {

    public boolean isUnlocked(ServerPlayer player) {
        return this.questGiver().canAcceptNPCQuest(player, this.quest());
    }

    public boolean isValid(MinecraftServer server) {
        RunecraftorySavedData savedData = RunecraftorySavedData.get(server);
        if (!savedData.npcHandler.doesNPCExist(this.questGiver().getUUID())) {
            return false;
        }
        return this.relevantNpcs().values().stream().allMatch(npc -> npc.stream().allMatch(savedData.npcHandler::doesNPCExist));
    }

    public ResourceLocation questId() {
        return NPCQuestDefinition.withUuid(this.quest, this.questGiver().getUUID());
    }
}
