package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CRunePoints implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CRunePoints> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_rp"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRunePoints> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CRunePoints decode(RegistryFriendlyByteBuf buf) {
            return new S2CRunePoints(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CRunePoints pkt) {
            buf.writeInt(pkt.rp);
        }
    };

    private final int rp;

    private S2CRunePoints(int rp) {
        this.rp = rp;
    }

    public S2CRunePoints(PlayerData data) {
        this.rp = data.getRunePoints();
    }

    public static void handle(S2CRunePoints pkt, Player player) {
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).setRunePoints(pkt.rp);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
