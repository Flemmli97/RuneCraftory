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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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

    public NPCSpawnData withId(@Nullable ResourceLocation id) {
        return new NPCSpawnData(this.profession(), Optional.ofNullable(id));
    }

    public NPCSpawnData withProfession(HolderLookup.Provider provider, @Nullable ResourceLocation profession) {
        if (profession == null)
            return new NPCSpawnData(Optional.empty(), this.npcDataId());
        return new NPCSpawnData(provider.lookupOrThrow(RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY)
                .get(ResourceKey.create(RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY, profession))
                .map(r -> r), this.npcDataId());
    }
}
