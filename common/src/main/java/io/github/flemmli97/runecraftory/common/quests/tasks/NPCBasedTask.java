package io.github.flemmli97.runecraftory.common.quests.tasks;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;

public interface NPCBasedTask {

    ResourceLocation ANY_NPC = RuneCraftory.modRes("any");

    ResourceLocation targetNPCId();
}
