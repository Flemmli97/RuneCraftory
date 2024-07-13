package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class S2CBossbarInfoRemove implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_bossbar_info_remove");

    private final UUID id;
    private final boolean immediate;

    public S2CBossbarInfoRemove(UUID id, boolean immediate) {
        this.id = id;
        this.immediate = immediate;
    }

    public static S2CBossbarInfoRemove read(FriendlyByteBuf buf) {
        return new S2CBossbarInfoRemove(buf.readUUID(), buf.readBoolean());
    }

    public static void handle(S2CBossbarInfoRemove pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        BossBarTracker.removeActiveBossbar(pkt.id, pkt.immediate);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
        buf.writeBoolean(this.immediate);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
