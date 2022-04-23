package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CRunePoints implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_rp");

    private final int rp;

    private S2CRunePoints(int rp) {
        this.rp = rp;
    }

    public S2CRunePoints(PlayerData data) {
        this.rp = data.getRunePoints();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.rp);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CRunePoints read(FriendlyByteBuf buf) {
        return new S2CRunePoints(buf.readInt());
    }

    public static void handle(S2CRunePoints pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setRunePoints(player, pkt.rp));
    }
}
