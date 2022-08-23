package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CPlayerStats implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_base_stats");

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

    public static S2CPlayerStats read(FriendlyByteBuf buf) {
        return new S2CPlayerStats(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(S2CPlayerStats pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            data.setStr(player, pkt.strength);
            data.setIntel(player, pkt.intel);
            data.setVit(player, pkt.vit);
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.strength);
        buf.writeFloat(this.intel);
        buf.writeFloat(this.vit);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
