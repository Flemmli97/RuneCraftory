package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CEntityDataSync {

    private final Type type;
    private final boolean flag;
    private final int entityID;

    public S2CEntityDataSync(int entityID, Type type, boolean flag) {
        this.entityID = entityID;
        this.type = type;
        this.flag = flag;
    }

    public static S2CEntityDataSync read(PacketBuffer buf) {
        return new S2CEntityDataSync(buf.readInt(), buf.readEnumValue(Type.class), buf.readBoolean());
    }

    public static void write(S2CEntityDataSync pkt, PacketBuffer buf) {
        buf.writeInt(pkt.entityID);
        buf.writeEnumValue(pkt.type);
        buf.writeBoolean(pkt.flag);
    }

    public static void handle(S2CEntityDataSync pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            Entity e = player.world.getEntityByID(pkt.entityID);
            if (e instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) e;
                e.getCapability(CapabilityInsts.EntityCap).ifPresent(cap -> {
                    switch (pkt.type) {
                        case POISON:
                            cap.setPoison(living, pkt.flag);
                            break;
                        case SLEEP:
                            cap.setSleeping(living, pkt.flag);
                            break;
                        case PARALYSIS:
                            cap.setParalysis(living, pkt.flag);
                            break;
                        case COLD:
                            cap.setCold(living, pkt.flag);
                            break;
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type {

        POISON,
        SLEEP,
        PARALYSIS,
        COLD,
        FATIGUE,
        SEAL
    }
}
