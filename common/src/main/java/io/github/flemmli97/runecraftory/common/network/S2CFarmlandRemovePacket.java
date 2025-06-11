package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public class S2CFarmlandRemovePacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CFarmlandRemovePacket> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_farmland_remove_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CFarmlandRemovePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CFarmlandRemovePacket decode(RegistryFriendlyByteBuf buf) {
            long packed = buf.readLong();
            if (buf.readBoolean())
                return new S2CFarmlandRemovePacket(packed, buf.readList(RegistryFriendlyByteBuf::readBlockPos));
            return new S2CFarmlandRemovePacket(packed);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CFarmlandRemovePacket pkt) {
            buf.writeLong(pkt.packedChunk);
            buf.writeBoolean(pkt.data != null);
            if (pkt.data != null)
                buf.writeCollection(pkt.data, RegistryFriendlyByteBuf::writeBlockPos);
        }
    };

    private final long packedChunk;
    private List<BlockPos> data;

    public S2CFarmlandRemovePacket(long packedChunk) {
        this.packedChunk = packedChunk;
    }

    public S2CFarmlandRemovePacket(long packedChunk, List<BlockPos> data) {
        this.packedChunk = packedChunk;
        this.data = data;
    }

    public static void handle(S2CFarmlandRemovePacket pkt) {
        if (pkt.data == null)
            ClientFarmlandHandler.INSTANCE.onChunkUnLoad(new ChunkPos(pkt.packedChunk));
        else
            pkt.data.forEach(ClientFarmlandHandler.INSTANCE::onFarmBlockRemove);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
