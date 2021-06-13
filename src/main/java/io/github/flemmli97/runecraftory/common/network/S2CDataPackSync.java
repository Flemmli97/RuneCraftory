package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CDataPackSync {

    private PacketBuffer buffer;

    private S2CDataPackSync(PacketBuffer buf) {
        this.buffer = buf;
    }

    public S2CDataPackSync() {
    }

    public static S2CDataPackSync read(PacketBuffer buf) {
        return new S2CDataPackSync(buf);
    }

    public static void write(S2CDataPackSync pkt, PacketBuffer buf) {
        DataPackHandler.toPacket(buf);
    }

    public static void handle(S2CDataPackSync pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            DataPackHandler.fromPacket(pkt.buffer);
        });
        ctx.get().setPacketHandled(true);
    }
}
