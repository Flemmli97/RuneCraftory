package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class S2CBossbarInfoAdd implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_bossbar_info_add");

    private final UUID id, musicID;
    private final ResourceLocation type;
    private final SoundEvent sound;

    public S2CBossbarInfoAdd(UUID id, UUID musicID, ResourceLocation type, SoundEvent sound) {
        this.id = id;
        this.musicID = musicID;
        this.type = type;
        this.sound = sound;
    }

    public static S2CBossbarInfoAdd read(FriendlyByteBuf buf) {
        return new S2CBossbarInfoAdd(buf.readUUID(), buf.readUUID(), buf.readBoolean() ? buf.readResourceLocation() : null,
                buf.readBoolean() ? Registry.SOUND_EVENT.get(buf.readResourceLocation()) : null);
    }

    public static void handle(S2CBossbarInfoAdd pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        BossBarTracker.addActiveBossbar(pkt.id, pkt.musicID, pkt.type, pkt.sound);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeUUID(this.musicID);
        buf.writeBoolean(this.type != null);
        if (this.type != null)
            buf.writeResourceLocation(this.type);
        buf.writeBoolean(this.sound != null);
        if (this.sound != null)
            buf.writeResourceLocation(Registry.SOUND_EVENT.getKey(this.sound));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
