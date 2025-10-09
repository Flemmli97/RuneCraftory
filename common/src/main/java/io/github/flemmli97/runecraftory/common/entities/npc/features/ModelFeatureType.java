package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.client.model.HumanoidModelLocations;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ModelFeatureType(Optional<ModelData> model,
                               Optional<ResourceLocation> animation) implements NPCFeature.NPCFeatureHolder<ModelFeatureType.ModelFeature> {

    public static MapCodec<ModelFeatureType> TYPE_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(ModelData.CODEC.optionalFieldOf("model").forGetter(ModelFeatureType::model),
                    ResourceLocation.CODEC.optionalFieldOf("animation").forGetter(ModelFeatureType::animation)
            ).apply(inst, ModelFeatureType::new));
    public static MapCodec<ModelFeature> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(ModelData.CODEC.optionalFieldOf("model").forGetter(ModelFeature::model),
                    ResourceLocation.CODEC.optionalFieldOf("animation").forGetter(ModelFeature::animation)
            ).apply(inst, ModelFeature::new));
    public static final StreamCodec<ByteBuf, ModelFeature> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ModelData.STREAM_CODEC), ModelFeature::model, ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ModelFeature::animation, ModelFeature::new);

    @Override
    public ModelFeature create(NPCEntity npc) {
        return new ModelFeature(this.model(), this.animation());
    }

    @Override
    public NPCFeatureType<ModelFeature> getType() {
        return RuneCraftoryNPCLooks.MODEL.get();
    }

    public record ModelFeature(Optional<ModelData> model, Optional<ResourceLocation> animation) implements NPCFeature {

        @Override
        public NPCFeatureType<?> type() {
            return RuneCraftoryNPCLooks.MODEL.get();
        }
    }

    public record ModelData(ResourceLocation model, String layerPrefix, Optional<ResourceLocation> texture) {

        public static Codec<ModelData> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(ResourceLocation.CODEC.fieldOf("model").forGetter(ModelData::model),
                        Codec.STRING.fieldOf("layerPrefix").forGetter(ModelData::layerPrefix),
                        ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(ModelData::texture)
                ).apply(inst, ModelData::new));

        public static final StreamCodec<ByteBuf, ModelData> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, ModelData::model, ByteBufCodecs.STRING_UTF8, ModelData::layerPrefix,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ModelData::texture, ModelData::new);

        public static ModelData textured(boolean slim, ResourceLocation texture) {
            return new ModelData(slim ? HumanoidModelLocations.DEFAULT_LOCATION_SLIM : HumanoidModelLocations.DEFAULT_LOCATION, "",
                    Optional.of(texture));
        }
    }
}
