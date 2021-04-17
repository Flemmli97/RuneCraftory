package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.AttackAABBRender;
import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.lib.EnumAABBType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CAttackDebug {

    private AxisAlignedBB aabb;
    private int duration;
    private EnumAABBType type;

    public S2CAttackDebug(AxisAlignedBB aabb) {
        this(aabb, 300, EnumAABBType.ATTACK);
    }

    public S2CAttackDebug(AxisAlignedBB aabb, EnumAABBType type) {
        this(aabb, 300, type);
    }

    public S2CAttackDebug(AxisAlignedBB aabb, int duration, EnumAABBType type) {
        this.aabb = aabb;
        this.duration = duration;
        this.type = type;
    }

    public static S2CAttackDebug read(PacketBuffer buf) {
        AxisAlignedBB aabb = new AxisAlignedBB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new S2CAttackDebug(aabb, buf.readInt(), buf.readEnumValue(EnumAABBType.class));
    }

    public static void write(S2CAttackDebug pkt, PacketBuffer buf) {
        buf.writeDouble(pkt.aabb.maxX);
        buf.writeDouble(pkt.aabb.maxY);
        buf.writeDouble(pkt.aabb.maxZ);
        buf.writeDouble(pkt.aabb.minX);
        buf.writeDouble(pkt.aabb.minY);
        buf.writeDouble(pkt.aabb.minZ);
        buf.writeInt(pkt.duration);
        buf.writeEnumValue(pkt.type);
    }

    public static void handle(S2CAttackDebug pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            AttackAABBRender.INST.addNewAABB(pkt.aabb, pkt.duration, pkt.type);
        });
        ctx.get().setPacketHandled(true);
    }
}
