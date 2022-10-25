package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.ShakeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record S2CScreenShake(int shakeDuration, float strength) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_screen_shake");

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
