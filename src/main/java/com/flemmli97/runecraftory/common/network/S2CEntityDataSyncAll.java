package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IEntityCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;


public class S2CEntityDataSyncAll {

    private PacketBuffer buffer;
    private Consumer<PacketBuffer> reader;
    private int entityID;

    public S2CEntityDataSyncAll(LivingEntity entity) {
        this.entityID = entity.getEntityId();
        IEntityCap cap = entity.getCapability(CapabilityInsts.EntityCap).orElseThrow(() -> new NullPointerException("Capability for " + entity + " is null. This shouldnt be"));
        this.reader = cap::writeAllToPkt;
    }

    private S2CEntityDataSyncAll(PacketBuffer buffer) {
        this.buffer = buffer;
    }

    public static S2CEntityDataSyncAll read(PacketBuffer buf) {
        return new S2CEntityDataSyncAll(buf);
    }

    public static void write(S2CEntityDataSyncAll pkt, PacketBuffer buf) {
        pkt.reader.accept(buf);
    }

    public static void handle(S2CEntityDataSyncAll pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            Entity e = player.world.getEntityByID(pkt.entityID);
            if (e instanceof LivingEntity) {
                e.getCapability(CapabilityInsts.EntityCap).ifPresent(cap -> cap.readFromPacket(pkt.buffer));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
