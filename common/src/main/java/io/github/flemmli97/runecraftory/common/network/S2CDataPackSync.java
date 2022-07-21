package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CDataPackSync implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_datapack_sync");

    private FriendlyByteBuf buffer;

    private S2CDataPackSync(FriendlyByteBuf buf) {
        this.buffer = buf;
    }

    public S2CDataPackSync() {
    }

    public static S2CDataPackSync read(FriendlyByteBuf buf) {
        return new S2CDataPackSync(new FriendlyByteBuf(buf.copy()));
    }

    public static void handle(S2CDataPackSync pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        DataPackHandler.fromPacket(pkt.buffer);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        DataPackHandler.toPacket(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
