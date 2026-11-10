package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.random.WeightedEntry;

public record OutfitFeatureType(
        WeightedIndexedTexture outfits) implements NPCFeature.NPCFeatureHolder<OutfitFeatureType.OutfitFeature> {

    private static final Codec<WeightedEntry.Wrapper<String>> DIRECT_RESOURCE = RecordCodecBuilder.create(inst ->
            inst.group(Codec.INT.fieldOf("weight").forGetter(d -> d.getWeight().asInt()),
                    Codec.STRING.fieldOf("outfit").forGetter(WeightedEntry.Wrapper::data)
            ).apply(inst, (weight, type) -> WeightedEntry.wrap(type, weight)));

    public static final MapCodec<OutfitFeatureType> TYPE_CODEC = WeightedIndexedTexture.CODEC.fieldOf("outfits").xmap(OutfitFeatureType::new, OutfitFeatureType::outfits);
    public static MapCodec<OutfitFeature> CODEC = IndexedTexture.CODEC.xmap(OutfitFeature::new, OutfitFeature::outfit);
    public static final StreamCodec<ByteBuf, OutfitFeature> STREAM_CODEC = IndexedTexture.STREAM_CODEC.map(OutfitFeature::new, OutfitFeature::outfit);

    @Override
    public OutfitFeature create(NPCEntity npc) {
        IndexedTexture texture = this.outfits.getRandom(npc.getRandom());
        return new OutfitFeature(texture);
    }

    @Override
    public NPCFeatureType<OutfitFeature> getType() {
        return RuneCraftoryNPCLooks.OUTFIT.get();
    }

    public record OutfitFeature(IndexedTexture outfit) implements NPCFeature {

        @Override
        public NPCFeatureType<OutfitFeature> type() {
            return RuneCraftoryNPCLooks.OUTFIT.get();
        }
    }
}