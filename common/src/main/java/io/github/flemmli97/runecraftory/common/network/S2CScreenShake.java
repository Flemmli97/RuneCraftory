package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.ShakeHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record S2CScreenShake(int shakeDuration, float strength) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_screen_shake");

    public static void sendAround(Entity entity, double range, int duration, float strength) {
        sendAround(entity.level, entity.position(), range, duration, strength);
    }

    public static void sendAround(Level level, Vec3 pos, double range, int duration, float strength) {
        if (level instanceof ServerLevel serverLevel) {
            AABB area = new AABB(pos.x() - 0.5, pos.y() - 0.5, pos.z() + 0.5, pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5).inflate(range);
            for (ServerPlayer player : serverLevel.players()) {
                if (!area.contains(player.getX(), player.getY(), player.getZ()))
                    continue;
                Platform.INSTANCE.sendToClient(new S2CScreenShake(duration, strength), player);
            }
        }
    }

    public static S2CScreenShake read(FriendlyByteBuf buf) {
        return new S2CScreenShake(buf.readInt(), buf.readFloat());
    }

    public static void handle(S2CScreenShake pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        ShakeHandler.shakeScreen(pkt.shakeDuration, pkt.strength);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.shakeDuration);
        buf.writeFloat(this.strength);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
