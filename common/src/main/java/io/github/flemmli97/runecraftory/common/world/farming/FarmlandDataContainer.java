package io.github.flemmli97.runecraftory.common.world.farming;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record FarmlandDataContainer(BlockPos pos, float growth, float quality, float size, int health, int defence,
                                    int ageProgress, int cropSizeProgress, float cropLevel) {

    public static FarmlandDataContainer fromBuffer(FriendlyByteBuf buf) {
        return new FarmlandDataContainer(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readInt(), buf.readFloat());
    }

    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeFloat(this.growth);
        buf.writeFloat(this.quality);
        buf.writeFloat(this.size);
        buf.writeInt(this.health);
        buf.writeInt(this.defence);
        buf.writeInt(this.ageProgress);
        buf.writeInt(this.cropSizeProgress);
        buf.writeFloat(this.cropLevel);
    }

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
