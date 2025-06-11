package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ShakeHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record S2CScreenShake(int shakeDuration, float strength) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CScreenShake> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_screen_shake"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CScreenShake> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CScreenShake decode(RegistryFriendlyByteBuf buf) {
            return new S2CScreenShake(buf.readInt(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CScreenShake pkt) {
            buf.writeInt(pkt.shakeDuration);
            buf.writeFloat(pkt.strength);
        }
    };

    public static void sendAround(Entity entity, double range, int duration, float strength) {
        sendAround(entity.level(), entity.position(), range, duration, strength);
    }

    public static void sendAround(Level level, Vec3 pos, double range, int duration, float strength) {
        if (level instanceof ServerLevel serverLevel) {
            AABB area = new AABB(pos.x() - 0.5, pos.y() - 0.5, pos.z() + 0.5, pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5).inflate(range);
            for (ServerPlayer player : serverLevel.players()) {
                if (!area.contains(player.getX(), player.getY(), player.getZ()))
                    continue;
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CScreenShake(duration, strength), player);
            }
        }
    }

    public static void handle(S2CScreenShake pkt, Player player) {
        if (player == null)
            return;
        ShakeHandler.shakeScreen(pkt.shakeDuration, pkt.strength);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
