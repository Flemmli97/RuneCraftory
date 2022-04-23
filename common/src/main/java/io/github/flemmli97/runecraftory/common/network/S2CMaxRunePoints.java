package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CMaxRunePoints implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_max_rp");

    private final float rpMax;

    private S2CMaxRunePoints(float rp) {
        this.rpMax = rp;
    }

    public S2CMaxRunePoints(PlayerData data) {
        this.rpMax = data.getMaxRunePointsRaw();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.rpMax);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CMaxRunePoints read(FriendlyByteBuf buf) {
        return new S2CMaxRunePoints(buf.readFloat());
    }

    public static void handle(S2CMaxRunePoints pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setMaxRunePoints(player, pkt.rpMax));
    }
}
