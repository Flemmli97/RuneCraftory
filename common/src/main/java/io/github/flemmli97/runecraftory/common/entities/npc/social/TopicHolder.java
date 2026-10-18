package io.github.flemmli97.runecraftory.common.entities.npc.social;

import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public record TopicHolder(ReloadableHolder<ConversationSet> holder, Topic topic) {

    public record Topic(String key, ConversationSet.Conversation conversation,
                        Map<ResourceLocation, Set<String>> participants) {
    }
}
