package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ColoredParticleData4f extends ColoredParticleData {

    public static MapCodec<ColoredParticleData4f> codec4f(ParticleType<ColoredParticleData4f> type) {
        return RecordCodecBuilder.mapCodec((builder) -> builder.group(
                        Codec.FLOAT.fieldOf("r").forGetter(ColoredParticleData::getRed),
                        Codec.FLOAT.fieldOf("g").forGetter(ColoredParticleData::getGreen),
                        Codec.FLOAT.fieldOf("b").forGetter(ColoredParticleData::getBlue),
                        Codec.FLOAT.fieldOf("alpha").forGetter(ColoredParticleData::getAlpha),
                        Codec.FLOAT.fieldOf("scale").forGetter(ColoredParticleData::getScale),
                        Codec.FLOAT.fieldOf("radius").forGetter(ColoredParticleData4f::getRadius),
                        Codec.FLOAT.fieldOf("speed").forGetter(ColoredParticleData4f::getSpeed),
                        Codec.FLOAT.fieldOf("offset").forGetter(ColoredParticleData4f::getOffset),
                        Codec.FLOAT.fieldOf("angle_increase").forGetter(ColoredParticleData4f::getAngleIncrease),
                        Codec.FLOAT.fieldOf("expansion").forGetter(ColoredParticleData4f::getExpansion))
                .apply(builder, (r, g, b, a, scale, radius, inc, offset, angle, exp) -> new ColoredParticleData4f(type, r, g, b, a, scale, radius, inc, offset, angle, exp)));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ColoredParticleData4f> streamCodec4f(ParticleType<ColoredParticleData4f> type) {
        return new StreamCodec<>() {
            @Override
            public ColoredParticleData4f decode(RegistryFriendlyByteBuf buf) {
                return new ColoredParticleData4f(type, buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                        buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, ColoredParticleData4f data) {
                buf.writeFloat(data.getRed());
                buf.writeFloat(data.getGreen());
                buf.writeFloat(data.getBlue());
                buf.writeFloat(data.getAlpha());
                buf.writeFloat(data.getScale());
                buf.writeFloat(data.radius);
                buf.writeFloat(data.speed);
                buf.writeFloat(data.offset);
                buf.writeFloat(data.angleIncrease);
                buf.writeFloat(data.expansion);
            }
        };
    }

    private final float radius;
    private final float speed;
    private final float offset;
    private final float angleIncrease;
    private final float expansion;

    public ColoredParticleData4f(ParticleType<ColoredParticleData4f> type, float red, float green, float blue, float alpha) {
        this(type, red, green, blue, alpha, 1, 0, 0, 0, 0, 0);
    }

    public ColoredParticleData4f(ParticleType<ColoredParticleData4f> type, float red, float green, float blue, float alpha, float scale, float radius, float speed, float offset, float angleInc, float expansion) {
        super(type, red, green, blue, alpha, scale);
        this.radius = radius;
        this.speed = speed;
        this.offset = offset;
        this.angleIncrease = angleInc;
        this.expansion = expansion;
    }

    public float getRadius() {
        return this.radius;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getOffset() {
        return this.offset;
    }

    public float getAngleIncrease() {
        return this.angleIncrease;
    }

    public float getExpansion() {
        return this.expansion;
    }

    public static class Builder {

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;

        private float scale = 1;
        private float radius;
        private float speed = 1;
        private float offset;
        private float angleIncrease;
        private float expansion;

        public Builder(float red, float green, float blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public Builder withScale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder withSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder circle(float radius, float increase) {
            this.radius = radius;
            this.angleIncrease = increase;
            return this;
        }

        public Builder withOffset(float offset) {
            this.offset = offset;
            return this;
        }

        public Builder expandCircle(float expansion) {
            this.expansion = expansion;
            return this;
        }

        public ColoredParticleData4f build(ParticleType<ColoredParticleData4f> type) {
            return new ColoredParticleData4f(type, this.red, this.green, this.blue, this.alpha, this.scale, this.radius, this.speed, this.offset, this.angleIncrease, this.expansion);
        }
    }
}