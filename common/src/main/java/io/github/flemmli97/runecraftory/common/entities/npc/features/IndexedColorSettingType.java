package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class IndexedColorSettingType implements NPCFeature.NPCFeatureHolder<IndexedColorSettingType.IndexedColorFeature> {

    public static final Function<Supplier<NPCFeatureType<IndexedColorFeature>>, MapCodec<IndexedColorSettingType>> TYPE_CODEC = type -> RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.INT.listOf().fieldOf("indices").forGetter(d -> d.indices),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, (indices, color) -> new IndexedColorSettingType(type, indices, color)));
    public static final Function<Supplier<NPCFeatureType<IndexedColorFeature>>, MapCodec<IndexedColorFeature>> CODEC = type -> RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.INT.fieldOf("index").forGetter(IndexedColorFeature::index),
                    Codec.INT.fieldOf("color").forGetter(IndexedColorFeature::color)
            ).apply(inst, (index, color) -> new IndexedColorFeature(type.get(), index, color)));
    public static final Function<Supplier<NPCFeatureType<IndexedColorFeature>>, StreamCodec<ByteBuf, IndexedColorFeature>> STREAM_CODEC = type -> StreamCodec.composite(
            ByteBufCodecs.INT, IndexedColorFeature::index, ByteBufCodecs.INT, IndexedColorFeature::color, (idx, color) -> new IndexedColorFeature(type.get(), idx, color));

    private final List<Integer> indices;
    private final ColorSetting color;
    private final Supplier<NPCFeatureType<IndexedColorFeature>> type;

    public IndexedColorSettingType(Supplier<NPCFeatureType<IndexedColorFeature>> type, List<Integer> indices, ColorSetting setting) {
        this.indices = indices;
        this.color = setting;
        this.type = type;
    }

    @Override
    public IndexedColorFeature create(EntityNPCBase npc) {
        int index = this.indices.isEmpty() ? 0 : this.indices.get(npc.getRandom().nextInt(this.indices.size()));
        return new IndexedColorFeature(this.type.get(), index, this.color.getRandom(npc.getRandom()));
    }

    @Override
    public NPCFeatureType<IndexedColorFeature> getType() {
        return this.type.get();
    }

    public static NPCFeatureType<IndexedColorFeature> createSimple(Supplier<NPCFeatureType<IndexedColorFeature>> type) {
        return new NPCFeatureType<>(TYPE_CODEC.apply(type), CODEC.apply(type), STREAM_CODEC.apply(type));
    }

    public record IndexedColorFeature(NPCFeatureType<IndexedColorFeature> type, int index,
                                      int color) implements NPCFeature {

    }
}