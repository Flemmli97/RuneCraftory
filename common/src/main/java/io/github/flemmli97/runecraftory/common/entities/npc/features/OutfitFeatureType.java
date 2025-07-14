package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record OutfitFeatureType(
        TypedIndexRange types) implements NPCFeature.NPCFeatureHolder<OutfitFeatureType.OutfitFeature> {

    public static final MapCodec<OutfitFeatureType> TYPE_CODEC = TypedIndexRange.CODEC.fieldOf("outfits").xmap(OutfitFeatureType::new, OutfitFeatureType::types);
    public static MapCodec<OutfitFeature> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.STRING.fieldOf("outfit").forGetter(OutfitFeature::outfit),
                    Codec.INT.fieldOf("index").forGetter(OutfitFeature::index)
            ).apply(inst, OutfitFeature::new));
    public static final StreamCodec<ByteBuf, OutfitFeature> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, OutfitFeature::outfit, ByteBufCodecs.INT, OutfitFeature::index, OutfitFeature::new);

    @Override
    public OutfitFeature create(NPCEntity npc) {
        Pair<String, Integer> style = this.types.getRandom(npc.getRandom());
        return new OutfitFeature(style.getFirst(), style.getSecond());
    }

    @Override
    public NPCFeatureType<OutfitFeature> getType() {
        return RuneCraftoryNPCLooks.OUTFIT.get();
    }

    public record OutfitFeature(String outfit, int index) implements NPCFeature {

        @Override
        public NPCFeatureType<OutfitFeature> type() {
            return RuneCraftoryNPCLooks.OUTFIT.get();
        }
    }
}