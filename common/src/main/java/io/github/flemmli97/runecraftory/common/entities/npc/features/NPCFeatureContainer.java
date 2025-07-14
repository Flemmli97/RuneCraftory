package io.github.flemmli97.runecraftory.common.entities.npc.features;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCFeatureContainer {

    private final Map<NPCFeatureType<?>, NPCFeature> map = new HashMap<>();
    public final Map<NPCFeatureType<?>, NPCFeature> view = Collections.unmodifiableMap(this.map);

    @SuppressWarnings("unchecked")
    public <T extends NPCFeature> T getFeature(NPCFeatureType<T> type) {
        return (T) this.map.get(type);
    }

    public void buildFromLooks(NPCEntity npc, Collection<NPCFeature.NPCFeatureHolder<?>> features) {
        this.map.clear();
        features.forEach(feat -> this.map.put(feat.getType(), feat.create(npc)));
    }

    public Tag save(HolderLookup.Provider provider) {
        return NPCFeature.FEATURE_CODEC.listOf().encodeStart(provider.createSerializationContext(NbtOps.INSTANCE),
                List.copyOf(this.map.values())).getOrThrow();
    }

    public NPCFeatureContainer read(Tag tag, HolderLookup.Provider provider) {
        this.map.clear();
        NPCFeature.FEATURE_CODEC.listOf().parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
                .promotePartial(RuneCraftory.LOGGER::error).getOrThrow()
                .forEach(feature -> this.map.put(feature.type(), feature));
        return this;
    }

    public void toBuffer(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.map.size());
        this.map.forEach((type, feat) -> NPCFeature.STREAM_CODEC.encode(buf, feat));
    }

    public NPCFeatureContainer fromBuffer(RegistryFriendlyByteBuf buf) {
        this.map.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            NPCFeature feat = NPCFeature.STREAM_CODEC.decode(buf);
            this.map.put(feat.type(), feat);
        }
        return this;
    }

    public void with(NPCFeatureContainer other) {
        this.map.clear();
        this.map.putAll(other.map);
    }
}
