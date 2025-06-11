package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class S2CDataPackSync implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CDataPackSync> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_datapack_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CDataPackSync> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CDataPackSync decode(RegistryFriendlyByteBuf buf) {
            return new S2CDataPackSync(new FriendlyByteBuf(buf.copy()));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CDataPackSync pkt) {
            buf.writeEnum(pkt.type);
            DataPackHandler.toPacket(buf, pkt.type);
        }
    };

    private FriendlyByteBuf buffer;

    private final SyncedType type;

    private S2CDataPackSync(FriendlyByteBuf buf) {
        this.type = buf.readEnum(SyncedType.class);
        this.buffer = buf;
    }

    public S2CDataPackSync(SyncedType type) {
        this.type = type;
    }

    public static void handle(S2CDataPackSync pkt) {
        DataPackHandler.fromPacket(pkt.type, pkt.buffer);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum SyncedType {
        ITEMSTATS,
        CROPS,
        FOOD,
    }
}
