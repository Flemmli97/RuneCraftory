package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CPlayerStats implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CPlayerStats> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_base_stats"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPlayerStats> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CPlayerStats decode(RegistryFriendlyByteBuf buf) {
            return new S2CPlayerStats(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CPlayerStats pkt) {
            buf.writeFloat(pkt.strength);
            buf.writeFloat(pkt.intel);
            buf.writeFloat(pkt.vit);
        }
    };

    private final float strength, intel, vit;

    private S2CPlayerStats(float strength, float intel, float vit) {
        this.strength = strength;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CPlayerStats(PlayerData data) {
        this.strength = data.getStr();
        this.intel = data.getIntel();
        this.vit = data.getVit();
    }

    public static void handle(S2CPlayerStats pkt, Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        data.setStr(pkt.strength);
        data.setIntel(pkt.intel);
        data.setVit(pkt.vit);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
