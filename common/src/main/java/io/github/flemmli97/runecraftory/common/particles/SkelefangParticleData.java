package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Random;

public class SkelefangParticleData implements ParticleOptions {

    private static final Random RANDOM = new Random();

    public static final MapCodec<SkelefangParticleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                    CodecUtils.stringEnumCodec(SkelefangBoneType.class, SkelefangBoneType.GENERIC).fieldOf("bone").forGetter(SkelefangParticleData::getBoneType),
                    Codec.FLOAT.fieldOf("init_x").forGetter(SkelefangParticleData::getInitialRotX),
                    Codec.FLOAT.fieldOf("init_y").forGetter(SkelefangParticleData::getInitialRotY),
                    Codec.FLOAT.fieldOf("rot_x").forGetter(SkelefangParticleData::getPitchSpin),
                    Codec.FLOAT.fieldOf("rot_y").forGetter(SkelefangParticleData::getYawSpin),
                    Codec.INT.fieldOf("max_ticks").forGetter(SkelefangParticleData::getMaxTime),
                    Codec.BOOL.fieldOf("gravity").forGetter(SkelefangParticleData::hasGravity))
            .apply(builder, (bone, initX, initY, rotX, rotY, maxTick, gravity) -> new SkelefangParticleData(bone, initX, initY, rotX, rotX, maxTick, gravity)));

    public static final StreamCodec<RegistryFriendlyByteBuf, SkelefangParticleData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SkelefangParticleData decode(RegistryFriendlyByteBuf buf) {
            return new SkelefangParticleData(buf.readEnum(SkelefangBoneType.class), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SkelefangParticleData data) {
            buf.writeEnum(data.getBoneType());
            buf.writeFloat(data.getInitialRotX());
            buf.writeFloat(data.getInitialRotY());
            buf.writeFloat(data.getPitchSpin());
            buf.writeFloat(data.getYawSpin());
            buf.writeInt(data.getMaxTime());
            buf.writeBoolean(data.hasGravity());
        }
    };

    private final SkelefangParticleData.SkelefangBoneType boneType;
    private final float initialRotX;
    private final float initialRotY;
    private final float pitchSpin;
    private final float yawSpin;
    private final int maxTime;
    private final boolean gravity;

    public SkelefangParticleData(SkelefangBoneType boneType, float initialRotX, float initialRotY, float pitchSpin, float yawSpin) {
        this(boneType, initialRotX, initialRotY, pitchSpin, yawSpin, (int) (RANDOM.nextFloat() * 10 + 15), true);
    }

    public SkelefangParticleData(SkelefangBoneType boneType, float initialRotX, float initialRotY, float pitchSpin, float yawSpin, int maxTime, boolean gravity) {
        super();
        this.boneType = boneType;
        this.initialRotX = initialRotX;
        this.initialRotY = initialRotY;
        this.pitchSpin = pitchSpin;
        this.yawSpin = yawSpin;
        this.maxTime = maxTime;
        this.gravity = gravity;
    }

    @Override
    public ParticleType<?> getType() {
        return RuneCraftoryParticles.SKELEFANG_BONES.get();
    }

    public SkelefangBoneType getBoneType() {
        return this.boneType;
    }

    public float getInitialRotX() {
        return this.initialRotX;
    }

    public float getInitialRotY() {
        return this.initialRotY;
    }

    public float getPitchSpin() {
        return this.pitchSpin;
    }

    public float getYawSpin() {
        return this.yawSpin;
    }

    public int getMaxTime() {
        return this.maxTime;
    }

    public boolean hasGravity() {
        return this.gravity;
    }

    public enum SkelefangBoneType {
        HEAD,
        NECK,
        FRONT,
        FRONT_RIBS,
        LEFT_LEG,
        RIGHT_LEG,
        BACK,
        BACK_RIBS,
        TAIL,
        TAIL_BASE,
        GENERIC,
        GENERIC2
    }
}