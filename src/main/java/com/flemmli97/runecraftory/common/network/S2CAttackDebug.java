package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.AttackAABBRender;
import com.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CAttackDebug {

    public AxisAlignedBB aabb;
    public int duration;

    public S2CAttackDebug(AxisAlignedBB aabb) {
        this(aabb, 300);
    }

    public S2CAttackDebug(AxisAlignedBB aabb, int duration) {
        this.aabb = aabb;
        this.duration = duration;
    }

    public static S2CAttackDebug read(PacketBuffer buf) {
        AxisAlignedBB aabb = new AxisAlignedBB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new S2CAttackDebug(aabb, buf.readInt());
    }

    public static void write(S2CAttackDebug pkt, PacketBuffer buf) {
        buf.writeDouble(pkt.aabb.maxX);
        buf.writeDouble(pkt.aabb.maxY);
        buf.writeDouble(pkt.aabb.maxZ);
        buf.writeDouble(pkt.aabb.minX);
        buf.writeDouble(pkt.aabb.minY);
        buf.writeDouble(pkt.aabb.minZ);
        buf.writeInt(pkt.duration);
    }

    public static void handle(S2CAttackDebug pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            AttackAABBRender.INST.addNewAABB(pkt.aabb, pkt.duration);
        });
        ctx.get().setPacketHandled(true);
    }
}
