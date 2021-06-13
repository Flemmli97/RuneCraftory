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

public class S2CMaxRunePoints {

    private final int rpMax;

    private S2CMaxRunePoints(int rp) {
        this.rpMax = rp;
    }

    public S2CMaxRunePoints(IPlayerCap cap) {
        this.rpMax = cap.getMaxRunePoints();
    }

    public static S2CMaxRunePoints read(PacketBuffer buf) {
        return new S2CMaxRunePoints(buf.readInt());
    }

    public static void write(S2CMaxRunePoints pkt, PacketBuffer buf) {
        buf.writeInt(pkt.rpMax);
    }

    public static void handle(S2CMaxRunePoints pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.setMaxRunePoints(player, pkt.rpMax));
        });
        ctx.get().setPacketHandled(true);
    }
}
