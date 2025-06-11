package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public class S2CBossbarInfoRemove implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CBossbarInfoRemove> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_bossbar_info_remove"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBossbarInfoRemove> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CBossbarInfoRemove decode(RegistryFriendlyByteBuf buf) {
            return new S2CBossbarInfoRemove(buf.readUUID(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CBossbarInfoRemove pkt) {
            buf.writeUUID(pkt.id);
            buf.writeBoolean(pkt.immediate);
        }
    };

    private final UUID id;
    private final boolean immediate;

    public S2CBossbarInfoRemove(UUID id, boolean immediate) {
        this.id = id;
        this.immediate = immediate;
    }

    public static void handle(S2CBossbarInfoRemove pkt) {
        BossBarTracker.removeActiveBossbar(pkt.id, pkt.immediate);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
