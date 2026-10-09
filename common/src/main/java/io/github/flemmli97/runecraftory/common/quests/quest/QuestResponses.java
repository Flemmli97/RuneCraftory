package io.github.flemmli97.runecraftory.common.quests.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record QuestResponses(Optional<ResourceLocation> startID, Optional<ResourceLocation> activeID,
                             Optional<ResourceLocation> endID) {

    public static final Codec<QuestResponses> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(ResourceLocation.CODEC.optionalFieldOf("start_id").forGetter(QuestResponses::startID),
                    ResourceLocation.CODEC.optionalFieldOf("active_id").forGetter(QuestResponses::activeID),
                    ResourceLocation.CODEC.optionalFieldOf("end_id").forGetter(QuestResponses::endID)
            ).apply(inst, QuestResponses::new));
}
