package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.ServerValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Map;

public class S2CSyncConfig implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSyncConfig> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_sync_config_values"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncConfig> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSyncConfig decode(RegistryFriendlyByteBuf buf) {
            S2CSyncConfig pkt = new S2CSyncConfig();
            pkt.buf = new FriendlyByteBuf(buf.copy());
            buf.clear();
            return pkt;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSyncConfig pkt) {
            Map<String, ServerValue<?>> configs = ServerValue.getSyncableValues();
            buf.writeInt(configs.size());
            configs.forEach((key, val) -> {
                buf.writeUtf(key);
                val.writeToBuffer(buf);
            });
        }
    };

    private FriendlyByteBuf buf;

    public static void handle(S2CSyncConfig pkt) {
        int size = pkt.buf.readInt();
        Map<String, ServerValue<?>> configs = ServerValue.getSyncableValues();
        for (int i = 0; i < size; i++) {
            ServerValue<?> val = configs.get(pkt.buf.readUtf());
            if (val != null)
                val.readFromBuffer(pkt.buf);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
