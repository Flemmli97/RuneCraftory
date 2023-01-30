package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandDataContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class S2CFarmlandUpdatePacket implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_farmland_update_packet");

    private final long packedChunk;
    private List<FarmlandData> data;
    private List<FarmlandDataContainer> holder;

    public S2CFarmlandUpdatePacket(long packedChunk, List<FarmlandData> data) {
        this.packedChunk = packedChunk;
        this.data = data;
    }

    private S2CFarmlandUpdatePacket(long packedChunk, List<FarmlandDataContainer> holder, boolean flag) {
        this.packedChunk = packedChunk;
        this.holder = holder;
    }

    public static S2CFarmlandUpdatePacket read(FriendlyByteBuf buf) {
        return new S2CFarmlandUpdatePacket(buf.readLong(), buf.readList(FarmlandDataContainer::fromBuffer), true);
    }

    public static void handle(S2CFarmlandUpdatePacket pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        ClientFarmlandHandler.INSTANCE.updateChunk(pkt.packedChunk, pkt.holder);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(this.packedChunk);
        buf.writeCollection(this.data, (b, d) -> d.writeToBuffer(b));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
