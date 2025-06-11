package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CMaxRunePoints implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CMaxRunePoints> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_max_rp"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMaxRunePoints> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CMaxRunePoints decode(RegistryFriendlyByteBuf buf) {
            return new S2CMaxRunePoints(buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CMaxRunePoints pkt) {
            buf.writeFloat(pkt.rpMax);
        }
    };

    private final float rpMax;

    private S2CMaxRunePoints(float rp) {
        this.rpMax = rp;
    }

    public S2CMaxRunePoints(PlayerData data) {
        this.rpMax = data.getMaxRunePointsRaw();
    }

    public static S2CMaxRunePoints read(RegistryFriendlyByteBuf buf) {
        return new S2CMaxRunePoints(buf.readFloat());
    }

    public static void handle(S2CMaxRunePoints pkt, Player player) {
        Platform.INSTANCE.getPlayerData(player).setMaxRunePoints(pkt.rpMax);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
