package io.github.flemmli97.runecraftory.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateParticleData implements ParticleOptions {

    public static final ParticleOptions.Deserializer<BlockStateParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public BlockStateParticleData fromCommand(ParticleType<BlockStateParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float yaw = reader.readFloat();
            reader.expect(' ');
            float pitch = reader.readFloat();
            reader.expect(' ');
            int duration = reader.readInt();
            reader.expect(' ');
            return new BlockStateParticleData(particleType, (new BlockStateParser(reader, false)).parse(false).getState(),
                    yaw, pitch, duration);
        }

        @Override
        public BlockStateParticleData fromNetwork(ParticleType<BlockStateParticleData> particleType, FriendlyByteBuf buffer) {
            return new BlockStateParticleData(particleType, Block.BLOCK_STATE_REGISTRY.byId(buffer.readVarInt()), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
        }
    };

    public static Codec<BlockStateParticleData> codec(ParticleType<BlockStateParticleData> type) {
        return BlockState.CODEC.xmap((blockState) -> new BlockStateParticleData(type, blockState), (BlockParticleOptionEx) -> BlockParticleOptionEx.state);
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
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(Block.BLOCK_STATE_REGISTRY.getId(this.state));
        buffer.writeFloat(this.yaw);
        buffer.writeFloat(this.pitch);
        buffer.writeInt(this.duration);
    }

    @Override
    public String writeToString() {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + "";
    }

    @Override
    public ParticleType<? extends BlockStateParticleData> getType() {
        return this.type;
    }
}
