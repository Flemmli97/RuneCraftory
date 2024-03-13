package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class DurationalParticleData extends ColoredParticleData {

    public static final Deserializer<DurationalParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public DurationalParticleData fromCommand(ParticleType<DurationalParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float a = reader.readFloat();
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            int duration = reader.readInt();
            return new DurationalParticleData(r, g, b, a, scale, duration);
        }

        @Override
        public DurationalParticleData fromNetwork(ParticleType<DurationalParticleData> type, FriendlyByteBuf buffer) {
            return new DurationalParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readInt());
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

    public static Codec<DurationalParticleData> codec() {
        return RecordCodecBuilder.create((builder) -> builder.group(
                        Codec.FLOAT.fieldOf("r").forGetter(ColoredParticleData::getRed),
                        Codec.FLOAT.fieldOf("g").forGetter(ColoredParticleData::getGreen),
                        Codec.FLOAT.fieldOf("b").forGetter(ColoredParticleData::getBlue),
                        Codec.FLOAT.fieldOf("alpha").forGetter(ColoredParticleData::getAlpha),
                        Codec.FLOAT.fieldOf("scale").forGetter(ColoredParticleData::getScale),
                        Codec.INT.fieldOf("duration").forGetter(DurationalParticleData::getDuration),
                        Codec.INT.fieldOf("entityAnchor").forGetter(DurationalParticleData::getEntityAnchor))
                .apply(builder, DurationalParticleData::new));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
        buffer.writeInt(this.getDuration());
        buffer.writeInt(this.getEntityAnchor());
    }

    public int getDuration() {
        return this.duration;
    }

    public int getEntityAnchor() {
        return this.entityAnchor;
    }
}