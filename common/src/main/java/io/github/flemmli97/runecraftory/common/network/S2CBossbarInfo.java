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

public class S2CBossbarInfo implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_bossbar_info");

    private final UUID id;
    private final ResourceLocation type;
    private final SoundEvent sound;
    private final boolean immediate;

    public S2CBossbarInfo(UUID id, ResourceLocation type, SoundEvent sound, boolean immediate) {
        this.id = id;
        this.type = type;
        this.sound = sound;
        this.immediate = immediate;
    }

    public S2CBossbarInfo(UUID id, boolean immediate) {
        this.id = id;
        this.type = null;
        this.sound = null;
        this.immediate = immediate;
    }

    public static S2CBossbarInfo read(FriendlyByteBuf buf) {
        return new S2CBossbarInfo(buf.readUUID(), buf.readBoolean() ? buf.readResourceLocation() : null,
                buf.readBoolean() ? Registry.SOUND_EVENT.get(buf.readResourceLocation()) : null, buf.readBoolean());
    }

    public static void handle(S2CBossbarInfo pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        if (pkt.type == null && pkt.sound == null)
            BossBarTracker.removeActiveBossbar(pkt.id, pkt.immediate);
        else
            BossBarTracker.addActiveBossbar(pkt.id, pkt.type, pkt.sound);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeBoolean(this.type != null);
        if (this.type != null)
            buf.writeResourceLocation(this.type);
        buf.writeBoolean(this.sound != null);
        if (this.sound != null)
            buf.writeResourceLocation(Registry.SOUND_EVENT.getKey(this.sound));
        buf.writeBoolean(this.immediate);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
