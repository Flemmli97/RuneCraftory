package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CLevelPkt implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CLevelPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CLevelPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CLevelPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CLevelPkt(new LevelExpPair(buf), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CLevelPkt pkt) {
            pkt.level.toPacket(buf);
            buf.writeInt(pkt.rp);
            buf.writeFloat(pkt.rpMax);
            buf.writeFloat(pkt.str);
            buf.writeFloat(pkt.intel);
            buf.writeFloat(pkt.vit);
        }
    };

    private final LevelExpPair level;
    private final int rp;
    private final float rpMax;
    private final float str;
    private final float intel;
    private final float vit;

    private S2CLevelPkt(LevelExpPair xp, int rp, float rpMax, float str, float intel, float vit) {
        this.level = xp;
        this.rp = rp;
        this.rpMax = rpMax;
        this.str = str;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CLevelPkt(PlayerData data) {
        this.level = data.getPlayerLevel();
        this.rp = data.getRunePoints();
        this.rpMax = data.getMaxRunePointsRaw();
        this.str = data.getStr();
        this.intel = data.getIntel();
        this.vit = data.getVit();
    }

    public static void handle(S2CLevelPkt pkt, Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        data.getPlayerLevel().from(pkt.level);
        data.setRunePoints(pkt.rp);
        data.setMaxRunePoints(pkt.rpMax);
        data.setStr(pkt.str);
        data.setIntel(pkt.intel);
        data.setVit(pkt.vit);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
