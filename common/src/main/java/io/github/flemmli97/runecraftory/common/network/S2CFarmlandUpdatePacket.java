package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandDataContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class S2CFarmlandUpdatePacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CFarmlandUpdatePacket> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_farmland_update_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CFarmlandUpdatePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CFarmlandUpdatePacket decode(RegistryFriendlyByteBuf buf) {
            return new S2CFarmlandUpdatePacket(buf.readLong(), buf.readList(FarmlandDataContainer.STREAM_CODEC), true);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CFarmlandUpdatePacket pkt) {
            buf.writeLong(pkt.packedChunk);
            buf.writeCollection(pkt.holder, FarmlandDataContainer.STREAM_CODEC);
        }
    };

    private final long packedChunk;
    private final List<FarmlandDataContainer> holder;

    public S2CFarmlandUpdatePacket(long packedChunk, List<FarmlandData> data) {
        this.packedChunk = packedChunk;
        this.holder = data.stream().map(FarmlandData::forSync).toList();
    }

    private S2CFarmlandUpdatePacket(long packedChunk, List<FarmlandDataContainer> holder, boolean flag) {
        this.packedChunk = packedChunk;
        this.holder = holder;
    }

    public static void handle(S2CFarmlandUpdatePacket pkt) {
        ClientFarmlandHandler.INSTANCE.updateChunk(pkt.packedChunk, pkt.holder);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
