package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CRunePoints {

    private final int rp;

    private S2CRunePoints(int rp) {
        this.rp = rp;
    }

    public S2CRunePoints(IPlayerCap cap) {
        this.rp = cap.getRunePoints();
    }

    public static S2CRunePoints read(PacketBuffer buf) {
        return new S2CRunePoints(buf.readInt());
    }

    public static void write(S2CRunePoints pkt, PacketBuffer buf) {
        buf.writeInt(pkt.rp);
    }

    public static void handle(S2CRunePoints pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.setRunePoints(player, pkt.rp));
        });
        ctx.get().setPacketHandled(true);
    }
}
