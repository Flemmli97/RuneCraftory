package io.github.flemmli97.runecraftory.integration.simplequest;

import io.github.flemmli97.simplequests.quest.types.QuestBase;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface SimpleQuestData {

    Map<ResourceLocation, QuestBase> runecraftory$getQuestboardQuests();

    void runecraftory$setQuestboardQuests(Map<ResourceLocation, QuestBase> map);
}
