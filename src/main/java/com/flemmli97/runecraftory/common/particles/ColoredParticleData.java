package com.flemmli97.runecraftory.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

public class ColoredParticleData implements IParticleData {

    public static Codec<ColoredParticleData> codec(ParticleType<ColoredParticleData> type) {
        return RecordCodecBuilder.create((builder) -> builder.group(
                Codec.FLOAT.fieldOf("r").forGetter((inst) -> inst.getRed()),
                Codec.FLOAT.fieldOf("g").forGetter((inst) -> inst.getGreen()),
                Codec.FLOAT.fieldOf("b").forGetter((inst) -> inst.getBlue()),
                Codec.FLOAT.fieldOf("alpha").forGetter((inst) -> inst.getAlpha()),
                Codec.FLOAT.fieldOf("scale").forGetter((inst) -> inst.getScale()))
                .apply(builder, (r, g, b, a, scale) -> new ColoredParticleData(type, r, g, b, a, scale)));
    }

    public static final IParticleData.IDeserializer<ColoredParticleData> DESERIALIZER = new IParticleData.IDeserializer<ColoredParticleData>() {
            @Override
            public ColoredParticleData deserialize(ParticleType<ColoredParticleData> type, StringReader reader) throws CommandSyntaxException {
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
                return new ColoredParticleData(type, r, g, b, a, scale);
            }

            @Override
            public ColoredParticleData read(ParticleType<ColoredParticleData> type, PacketBuffer buffer) {
                return new ColoredParticleData(type, buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            }
        };


    private final ParticleType<ColoredParticleData> type;
    private float red;
    private float green;
    private float blue;
    private float alpha;
    private float scale;

    public ColoredParticleData(ParticleType<ColoredParticleData> type, float red, float green, float blue, float alpha) {
        this(type, red, green, blue, alpha, 1);
    }

    public ColoredParticleData(ParticleType<ColoredParticleData> type, float red, float green, float blue, float alpha, float scale) {
        this.type = type;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.scale = scale;
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.alpha);
        buffer.writeFloat(this.scale);
    }

    @Override
    public String getParameters() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()).toString();
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public float getScale() {
        return this.scale;
    }
}