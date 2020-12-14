package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CStats {

    private final int rp;

    private S2CStats(int rp) {
        this.rp = rp;
    }

    public S2CStats(IPlayerCap cap) {
        this.rp = cap.getRunePoints();
    }


    public static S2CAttackDebug read(PacketBuffer buf) {
        AxisAlignedBB aabb = new AxisAlignedBB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new S2CAttackDebug(aabb, buf.readInt());
    }

    public static void write(S2CAttackDebug pkt, PacketBuffer buf) {

    }

    public static void handle(S2CAttackDebug pkt, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().setPacketHandled(true);
    }
}
