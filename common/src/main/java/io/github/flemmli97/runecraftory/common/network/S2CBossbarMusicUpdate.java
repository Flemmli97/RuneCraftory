package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class S2CBossbarMusicUpdate implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_bossbar_music_update");

    private final UUID id, musicID;
    private final boolean stop;

    public S2CBossbarMusicUpdate(UUID uuid, UUID musicID, boolean pause) {
        this.id = uuid;
        this.musicID = musicID;
        this.stop = pause;
    }

    public static S2CBossbarMusicUpdate read(FriendlyByteBuf buf) {
        return new S2CBossbarMusicUpdate(buf.readUUID(), buf.readUUID(), buf.readBoolean());
    }

    public static void handle(S2CBossbarMusicUpdate pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        BossBarTracker.updateMusic(pkt.id, pkt.musicID, pkt.stop);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeUUID(this.musicID);
        buf.writeBoolean(this.stop);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
