package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateParticleData implements ParticleOptions {

    public static MapCodec<BlockStateParticleData> codec(ParticleType<BlockStateParticleData> type) {
        return BlockState.CODEC.fieldOf("state").xmap((blockState) -> new BlockStateParticleData(type, blockState), (BlockParticleOptionEx) -> BlockParticleOptionEx.state);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, BlockStateParticleData> streamCodec(ParticleType<BlockStateParticleData> type) {
        return new StreamCodec<>() {
            @Override
            public BlockStateParticleData decode(RegistryFriendlyByteBuf buf) {
                return new BlockStateParticleData(type, ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).decode(buf),
                        buf.readFloat(), buf.readFloat(), buf.readInt());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, BlockStateParticleData data) {
                ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).encode(buf, data.state);
                buf.writeFloat(data.yaw);
                buf.writeFloat(data.pitch);
                buf.writeInt(data.duration);
            }
        };
    }

    private final ParticleType<? extends BlockStateParticleData> type;
    private final BlockState state;
    private final float yaw, pitch;
    private final int duration;

    public BlockStateParticleData(ParticleType<? extends BlockStateParticleData> type, BlockState state) {
        this(type, state, 0, 0, 40);
    }

    public BlockStateParticleData(ParticleType<? extends BlockStateParticleData> type, BlockState state, float yaw, float pitch, int duration) {
        this.type = type;
        this.state = state;
        this.yaw = yaw;
        this.pitch = pitch;
        this.duration = duration;
    }

    public BlockState getState() {
        return this.state;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public ParticleType<? extends BlockStateParticleData> getType() {
        return this.type;
    }
}
