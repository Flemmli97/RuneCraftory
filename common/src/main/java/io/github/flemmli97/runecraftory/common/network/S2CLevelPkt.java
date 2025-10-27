package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CLevelPkt implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CLevelPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CLevelPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CLevelPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CLevelPkt(new XpLevelHolder(buf), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CLevelPkt pkt) {
            pkt.level.toPacket(buf);
            buf.writeInt(pkt.rp);
        }
    };

    private final XpLevelHolder level;
    private final int rp;

    private S2CLevelPkt(XpLevelHolder xp, int rp) {
        this.level = xp;
        this.rp = rp;
    }

    public S2CLevelPkt(PlayerData data) {
        this.level = data.getPlayerLevel();
        this.rp = data.getRunePoints();
    }

    public static void handle(S2CLevelPkt pkt, Player player) {
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        data.getPlayerLevel().from(pkt.level);
        data.setRunePoints(pkt.rp);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
