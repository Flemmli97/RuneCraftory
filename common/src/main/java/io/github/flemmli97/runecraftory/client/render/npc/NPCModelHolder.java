package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.client.model.HumanoidModelLocations;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;

public class NPCModelHolder {

    private final Pair<ResourceLocation, ResourceLocation> location;
    private final EnumMap<NPCTextureLayer.ModelType, HumanoidBasedModel<?>> models = new EnumMap<>(NPCTextureLayer.ModelType.class);

    public NPCModelHolder(Pair<ResourceLocation, ResourceLocation> location) {
        this.location = location;
        for (NPCTextureLayer.ModelType modelType : NPCTextureLayer.ModelType.values()) {
            this.models.put(modelType, new HumanoidBasedModel<>(location.getFirst(),
                    location.getSecond() == null ? HumanoidModelLocations.DEFAULT_NPC_ANIMATION : location.getSecond(), modelType.expand));
        }
    }

    public Pair<ResourceLocation, ResourceLocation> getLocation() {
        return this.location;
    }

    @SuppressWarnings("unchecked")
    public <T extends NPCEntity> HumanoidBasedModel<T> get(NPCTextureLayer.ModelType modelType) {
        return (HumanoidBasedModel<T>) this.models.get(modelType);
    }
}
