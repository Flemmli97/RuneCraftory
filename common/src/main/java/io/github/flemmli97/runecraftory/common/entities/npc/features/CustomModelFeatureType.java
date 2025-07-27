package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record CustomModelFeatureType(List<Pair<ResourceLocation, List<ResourceLocation>>> models,
                                     Location location) implements NPCFeature.NPCFeatureHolder<CustomModelFeatureType.ModelFeature> {

    private static final Codec<Pair<ResourceLocation, List<ResourceLocation>>> VALUE_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ResourceLocation.CODEC.fieldOf("model").forGetter(Pair::getFirst),
                            ExtraCodecs.nonEmptyList(ResourceLocation.CODEC.listOf()).fieldOf("textures").forGetter(Pair::getSecond))
                    .apply(instance, Pair::of));
    public static final MapCodec<CustomModelFeatureType> TYPE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(ExtraCodecs.nonEmptyList(VALUE_CODEC.listOf()).fieldOf("models").forGetter(CustomModelFeatureType::models),
                            CodecUtils.stringEnumCodec(Location.class, null).fieldOf("location").forGetter(CustomModelFeatureType::location))
                    .apply(instance, CustomModelFeatureType::new));
    public static MapCodec<ModelFeature> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(ResourceLocation.CODEC.fieldOf("model").forGetter(ModelFeature::model),
                            ResourceLocation.CODEC.fieldOf("model").forGetter(ModelFeature::texture),
                            CodecUtils.stringEnumCodec(Location.class, null).fieldOf("location").forGetter(ModelFeature::location))
                    .apply(instance, ModelFeature::new));
    public static final StreamCodec<ByteBuf, ModelFeature> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC,
            ModelFeature::model, ResourceLocation.STREAM_CODEC, ModelFeature::texture,
            StreamCodecUtils.ofEnum(Location.class), ModelFeature::location, ModelFeature::new);

    @Override
    public ModelFeature create(NPCEntity npc) {
        if (this.models.isEmpty())
            throw new IllegalStateException("No models defined!");
        Pair<ResourceLocation, List<ResourceLocation>> selected = this.models.get(npc.getRandom().nextInt(this.models.size()));
        return new ModelFeature(selected.getFirst(), selected.getSecond().get(npc.getRandom().nextInt(selected.getSecond().size())), this.location());
    }

    @Override
    public NPCFeatureType<ModelFeature> getType() {
        return RuneCraftoryNPCLooks.MODEL.get();
    }

    public record ModelFeature(ResourceLocation model, ResourceLocation texture,
                               Location location) implements NPCFeature {

        @Override
        public NPCFeatureType<ModelFeature> type() {
            return RuneCraftoryNPCLooks.MODEL.get();
        }
    }

    public enum Location {
        HEAD,
        BODY,
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG
    }
}