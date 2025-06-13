package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class DurationalParticleData extends ColoredParticleData {

    public static final MapCodec<DurationalParticleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(ColoredParticleData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ColoredParticleData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ColoredParticleData::getBlue),
                    Codec.FLOAT.fieldOf("alpha").forGetter(ColoredParticleData::getAlpha),
                    Codec.FLOAT.fieldOf("scale").forGetter(ColoredParticleData::getScale),
                    Codec.INT.fieldOf("duration").forGetter(DurationalParticleData::getDuration),
                    Codec.INT.fieldOf("entity_anchor").forGetter(DurationalParticleData::getEntityAnchor))
            .apply(builder, DurationalParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DurationalParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public DurationalParticleData decode(RegistryFriendlyByteBuf buf) {
            return new DurationalParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, DurationalParticleData data) {
            buf.writeFloat(data.getRed());
            buf.writeFloat(data.getGreen());
            buf.writeFloat(data.getBlue());
            buf.writeFloat(data.getAlpha());
            buf.writeFloat(data.getScale());
            buf.writeInt(data.getDuration());
            buf.writeInt(data.getEntityAnchor());
        }
    };

    private final int duration, entityAnchor;

    public DurationalParticleData(float red, float green, float blue, float alpha, float scale, int duration) {
        this(red, green, blue, alpha, scale, duration, -1);
    }

    public DurationalParticleData(float red, float green, float blue, float alpha, float scale, int duration, int entityAnchor) {
        super(ModParticles.DURATIONAL_PARTICLE.get(), red, green, blue, alpha, scale);
        this.duration = duration;
        this.entityAnchor = entityAnchor;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getEntityAnchor() {
        return this.entityAnchor;
    }
}