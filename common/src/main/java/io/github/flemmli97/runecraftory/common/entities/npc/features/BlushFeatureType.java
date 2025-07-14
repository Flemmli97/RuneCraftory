package io.github.flemmli97.runecraftory.common.entities.npc.features;

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

public class BlushFeatureType implements NPCFeature.NPCFeatureHolder<BlushFeatureType.BlushFeature> {

    public static MapCodec<BlushFeatureType> TYPE_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.FLOAT.fieldOf("chance").forGetter(d -> d.chance),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.setting)
            ).apply(inst, BlushFeatureType::new));
    public static MapCodec<BlushFeature> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.BOOL.fieldOf("blush").forGetter(BlushFeature::blush),
                    Codec.INT.fieldOf("color").forGetter(BlushFeature::color)
            ).apply(inst, BlushFeature::new));
    public static final StreamCodec<ByteBuf, BlushFeature> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, BlushFeature::blush, ByteBufCodecs.INT, BlushFeature::color, BlushFeature::new);

    private final float chance;
    private final ColorSetting setting;

    public BlushFeatureType(float chance, ColorSetting setting) {
        this.chance = chance;
        this.setting = setting;
    }

    @Override
    public BlushFeature create(NPCEntity npc) {
        return new BlushFeature(npc.getRandom().nextFloat() < this.chance, this.setting.getRandom(npc.getRandom()));
    }

    @Override
    public NPCFeatureType<BlushFeature> getType() {
        return RuneCraftoryNPCLooks.BLUSH.get();
    }

    public record BlushFeature(boolean blush, int color) implements NPCFeature {

        @Override
        public NPCFeatureType<?> type() {
            return RuneCraftoryNPCLooks.BLUSH.get();
        }
    }
}
