package io.github.flemmli97.runecraftory.common.world.data.farming;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record FarmlandDataContainer(BlockPos pos, float growth, float quality, float size, int health, int defence,
                                    int ageProgress, int cropSizeProgress, float cropLevel) {

    public static final StreamCodec<FriendlyByteBuf, FarmlandDataContainer> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FarmlandDataContainer decode(FriendlyByteBuf buf) {
            return new FarmlandDataContainer(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt(),
                    buf.readInt(), buf.readInt(), buf.readFloat());
        }

        @Override
        public void encode(FriendlyByteBuf buf, FarmlandDataContainer data) {
            buf.writeBlockPos(data.pos);
            buf.writeFloat(data.growth);
            buf.writeFloat(data.quality);
            buf.writeFloat(data.size);
            buf.writeInt(data.health);
            buf.writeInt(data.defence);
            buf.writeInt(data.ageProgress);
            buf.writeInt(data.cropSizeProgress);
            buf.writeFloat(data.cropLevel);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof FarmlandDataContainer cont)
            return cont.pos.equals(this.pos);
        return false;
    }

    @Override
    public int hashCode() {
        return this.pos.hashCode();
    }
}
