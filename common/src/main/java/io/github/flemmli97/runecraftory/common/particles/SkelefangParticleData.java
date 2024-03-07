package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;

import java.util.Random;

public class SkelefangParticleData implements ParticleOptions {

    private static final Random RANDOM = new Random();

    public static final Deserializer<SkelefangParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public SkelefangParticleData fromCommand(ParticleType<SkelefangParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String bone = reader.readString();
            SkelefangBoneType boneType;
            try {
                boneType = SkelefangBoneType.valueOf(bone);
            } catch (IllegalArgumentException e) {
                throw new SimpleCommandExceptionType(new TextComponent("No such bonetype " + bone)).create();
            }
            reader.expect(' ');
            float initX = reader.readFloat();
            reader.expect(' ');
            float initY = reader.readFloat();
            reader.expect(' ');
            float rotX = reader.readFloat();
            reader.expect(' ');
            float rotY = reader.readFloat();
            return new SkelefangParticleData(boneType, initX, initY, rotX, rotY);
        }

        @Override
        public SkelefangParticleData fromNetwork(ParticleType<SkelefangParticleData> type, FriendlyByteBuf buffer) {
            return new SkelefangParticleData(buffer.readEnum(SkelefangBoneType.class), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                    buffer.readInt(), buffer.readBoolean());
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

    public static Codec<SkelefangParticleData> codec() {
        return RecordCodecBuilder.create((builder) -> builder.group(
                        CodecHelper.enumCodec(SkelefangBoneType.class, SkelefangBoneType.GENERIC).fieldOf("bone").forGetter(SkelefangParticleData::getBoneType),
                        Codec.FLOAT.fieldOf("initX").forGetter(SkelefangParticleData::getInitialRotX),
                        Codec.FLOAT.fieldOf("initY").forGetter(SkelefangParticleData::getInitialRotY),
                        Codec.FLOAT.fieldOf("rotX").forGetter(SkelefangParticleData::getPitchSpin),
                        Codec.FLOAT.fieldOf("rotY").forGetter(SkelefangParticleData::getYawSpin),
                        Codec.INT.fieldOf("maxTicks").forGetter(SkelefangParticleData::getMaxTime),
                        Codec.BOOL.fieldOf("gravity").forGetter(SkelefangParticleData::hasGravity))
                .apply(builder, (bone, initX, initY, rotX, rotY, maxTick, gravity) -> new SkelefangParticleData(bone, initX, initY, rotX, rotX, maxTick, gravity)));
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SKELEFANG_BONES.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.getBoneType());
        buffer.writeFloat(this.getInitialRotX());
        buffer.writeFloat(this.getInitialRotY());
        buffer.writeFloat(this.getPitchSpin());
        buffer.writeFloat(this.getYawSpin());
        buffer.writeInt(this.getMaxTime());
        buffer.writeBoolean(this.hasGravity());
    }

    @Override
    public String writeToString() {
        return PlatformUtils.INSTANCE.particles().getIDFrom(this.getType()).toString();
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