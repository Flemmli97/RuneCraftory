package io.github.flemmli97.runecraftory.common.quests.tasks;

import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface ResolvedNPCBasedTask {

    @Nullable
    ResourceLocation dialogue();

    @Nullable
    NPCEntity getNpc(Level level);

    default Component progressComponent(QuestProgress progress, String taskName) {
        return null;
    }
}
