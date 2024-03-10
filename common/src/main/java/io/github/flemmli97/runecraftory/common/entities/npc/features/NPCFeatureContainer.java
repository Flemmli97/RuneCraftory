package io.github.flemmli97.runecraftory.common.entities.npc.features;

import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCFeatureContainer {

    private final Map<NPCFeatureType<?>, NPCFeature> map = new HashMap<>();
    public final Map<NPCFeatureType<?>, NPCFeature> view = Collections.unmodifiableMap(this.map);

    @SuppressWarnings("unchecked")
    public <T extends NPCFeature> T getFeature(NPCFeatureType<?> type) {
        return (T) this.map.get(type);
    }

    public void buildFromLooks(EntityNPCBase npc, List<NPCFeatureHolder<?>> features) {
        this.map.clear();
        features.forEach(feat -> this.map.put(feat.getType(), feat.create(npc)));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        this.map.forEach((type, feat) -> {
            Tag save = feat.save();
            tag.put(type.getRegistryName().toString(), save == null ? IntTag.valueOf(0) : save);
        });
        return tag;
    }

    public void read(CompoundTag tag) {
        this.map.clear();
        tag.getAllKeys().forEach(key -> {
            NPCFeatureType<?> t = ModNPCLooks.NPC_FEATURE_REGISTRY.get()
                    .getFromId(new ResourceLocation(key));
            this.map.put(t, t.load.apply(tag.get(key)));
        });
    }
}
