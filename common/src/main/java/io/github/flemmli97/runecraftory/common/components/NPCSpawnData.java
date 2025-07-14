package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record NPCSpawnData(Optional<Holder<NPCProfession>> profession, Optional<ResourceLocation> npcDataId) {

    public static final NPCSpawnData DEFAULT = new NPCSpawnData(Optional.empty(), Optional.empty());

    public static final Codec<NPCSpawnData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(RuneCraftoryNPCProfessions.PROFESSIONS.registry().holderByNameCodec().optionalFieldOf("profession").forGetter(NPCSpawnData::profession),
                    ResourceLocation.CODEC.optionalFieldOf("npc_data_id").forGetter(NPCSpawnData::npcDataId)
            ).apply(instance, NPCSpawnData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, NPCSpawnData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY)), NPCSpawnData::profession,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), NPCSpawnData::npcDataId, NPCSpawnData::new);

    public NPCSpawnData cycleProfession(HolderLookup.Provider provider) {
        List<Holder.Reference<NPCProfession>> professions = provider.lookupOrThrow(RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY).listElements()
                .sorted(Comparator.comparing(Holder::getRegisteredName)).toList();
        if (professions.isEmpty())
            return new NPCSpawnData(Optional.empty(), this.npcDataId);
        if (this.profession.isEmpty())
            return new NPCSpawnData(Optional.ofNullable(professions.getFirst()), this.npcDataId);
        Holder<NPCProfession> next = null;
        for (int i = 0; i < professions.size(); i++) {
            if (professions.get(i).is(this.profession.get())) {
                next = professions.get((i + 1) % professions.size());
                break;
            }
        }
        return new NPCSpawnData(Optional.ofNullable(next), this.npcDataId);
    }

    public NPCSpawnData withId(@Nullable ResourceLocation id) {
        return new NPCSpawnData(this.profession(), Optional.ofNullable(id));
    }
}
