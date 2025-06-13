package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
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

public record NPCSpawnData(Optional<Holder<NPCJob>> job, Optional<ResourceLocation> npcDataId) {

    public static final NPCSpawnData DEFAULT = new NPCSpawnData(Optional.empty(), Optional.empty());

    public static final Codec<NPCSpawnData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ModNPCJobs.JOBS.registry().holderByNameCodec().optionalFieldOf("job").forGetter(NPCSpawnData::job),
                    ResourceLocation.CODEC.optionalFieldOf("npc_data_id").forGetter(NPCSpawnData::npcDataId)
            ).apply(instance, NPCSpawnData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, NPCSpawnData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(ModNPCJobs.JOB_REGISTRY_KEY)), NPCSpawnData::job,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), NPCSpawnData::npcDataId, NPCSpawnData::new);

    public NPCSpawnData cycleJob(HolderLookup.Provider provider) {
        List<Holder.Reference<NPCJob>> jobs = provider.lookupOrThrow(ModNPCJobs.JOB_REGISTRY_KEY).listElements()
                .sorted(Comparator.comparing(Holder::getRegisteredName)).toList();
        if (jobs.isEmpty())
            return new NPCSpawnData(Optional.empty(), this.npcDataId);
        if (this.job.isEmpty())
            return new NPCSpawnData(Optional.ofNullable(jobs.getFirst()), this.npcDataId);
        Holder<NPCJob> next = null;
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).is(this.job.get())) {
                next = jobs.get((i + 1) % jobs.size());
                break;
            }
        }
        return new NPCSpawnData(Optional.ofNullable(next), this.npcDataId);
    }

    public NPCSpawnData withId(@Nullable ResourceLocation id) {
        return new NPCSpawnData(this.job(), Optional.ofNullable(id));
    }
}
