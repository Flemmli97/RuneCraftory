package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public class S2CFarmlandRemovePacket implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_farmland_remove_packet");

    private final long packedChunk;
    private List<BlockPos> data;

    public S2CFarmlandRemovePacket(long packedChunk) {
        this.packedChunk = packedChunk;
    }

    public S2CFarmlandRemovePacket(long packedChunk, List<BlockPos> data) {
        this.packedChunk = packedChunk;
        this.data = data;
    }

    public static S2CFarmlandRemovePacket read(FriendlyByteBuf buf) {
        long packed = buf.readLong();
        if (buf.readBoolean())
            return new S2CFarmlandRemovePacket(packed, buf.readList(FriendlyByteBuf::readBlockPos));
        return new S2CFarmlandRemovePacket(packed);
    }

    public static void handle(S2CFarmlandRemovePacket pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        if (pkt.data == null)
            ClientFarmlandHandler.INSTANCE.onChunkUnLoad(new ChunkPos(pkt.packedChunk));
        else
            pkt.data.forEach(ClientFarmlandHandler.INSTANCE::onFarmBlockRemove);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(this.packedChunk);
        buf.writeBoolean(this.data != null);
        if (this.data != null)
            buf.writeCollection(this.data, FriendlyByteBuf::writeBlockPos);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
