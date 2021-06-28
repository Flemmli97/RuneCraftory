package io.github.flemmli97.runecraftory.common.particles;

import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class ColoredParticleData4f extends ColoredParticleData {

    public static Codec<ColoredParticleData4f> codec4f(ParticleType<ColoredParticleData4f> type) {
        return RecordCodecBuilder.create((builder) -> builder.group(
                Codec.FLOAT.fieldOf("r").forGetter((inst) -> inst.getRed()),
                Codec.FLOAT.fieldOf("g").forGetter((inst) -> inst.getGreen()),
                Codec.FLOAT.fieldOf("b").forGetter((inst) -> inst.getBlue()),
                Codec.FLOAT.fieldOf("alpha").forGetter((inst) -> inst.getAlpha()),
                Codec.FLOAT.fieldOf("scale").forGetter((inst) -> inst.getScale()),
                Codec.FLOAT.fieldOf("radius").forGetter((inst) -> inst.getRadius()),
                Codec.FLOAT.fieldOf("inc").forGetter((inst) -> inst.getInc()),
                Codec.FLOAT.fieldOf("offset").forGetter((inst) -> inst.getOffset()),
                Codec.FLOAT.fieldOf("angleInc").forGetter((inst) -> inst.getAngle()))
                .apply(builder, (r, g, b, a, scale, radius, inc, offset, angle) -> new ColoredParticleData4f(type, r, g, b, a, scale, radius, inc, offset, angle)));
    }

    public static final IParticleData.IDeserializer<ColoredParticleData4f> DESERIALIZER = new IParticleData.IDeserializer<ColoredParticleData4f>() {
        @Override
        public ColoredParticleData4f deserialize(ParticleType<ColoredParticleData4f> type, StringReader reader) throws CommandSyntaxException {
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
            float radius = reader.readFloat();
            reader.expect(' ');
            float inc = reader.readFloat();
            reader.expect(' ');
            float offset = reader.readFloat();
            reader.expect(' ');
            float angle = reader.readFloat();
            return new ColoredParticleData4f(type, r, g, b, a, scale, radius, inc, offset, angle);
        }

        @Override
        public ColoredParticleData4f read(ParticleType<ColoredParticleData4f> type, PacketBuffer buffer) {
            return new ColoredParticleData4f(type, buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

    private float radius, inc, offset, angle;

    public ColoredParticleData4f(ParticleType<ColoredParticleData4f> type, float red, float green, float blue, float alpha) {
        this(type, red, green, blue, alpha, 1, 0, 0, 0, 0);
    }

    public ColoredParticleData4f(ParticleType<ColoredParticleData4f> type, float red, float green, float blue, float alpha, float scale, float radius, float inc, float offset, float angleInc) {
        super(type, red, green, blue, alpha, scale);
        this.radius = radius;
        this.inc = inc;
        this.offset = offset;
        this.angle = angleInc;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.getRed());
        buffer.writeFloat(this.getGreen());
        buffer.writeFloat(this.getBlue());
        buffer.writeFloat(this.getAlpha());
        buffer.writeFloat(this.getScale());
        buffer.writeFloat(this.radius);
        buffer.writeFloat(this.inc);
        buffer.writeFloat(this.offset);
        buffer.writeFloat(this.angle);
    }

    public float getRadius() {
        return this.radius;
    }

    public float getInc() {
        return this.inc;
    }

    public float getOffset() {
        return this.offset;
    }

    public float getAngle() {
        return this.angle;
    }
}