package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class C2SOpenInfo {

    private final Type type;

    public C2SOpenInfo(Type type){
        this.type = type;
    }

    public static C2SOpenInfo read(PacketBuffer buf) {
        return new C2SOpenInfo(buf.readEnumValue(Type.class));
    }

    public static void write(C2SOpenInfo pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.type);
    }

    public static void handle(C2SOpenInfo pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                switch (pkt.type) {
                    case MAIN:
                        NetworkHooks.openGui(player, ContainerInfoScreen.create(), b->b.writeByte(0));
                        break;
                    case SUB:
                        NetworkHooks.openGui(player, ContainerInfoScreen.create(), b->b.writeByte(1));
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type{
        MAIN,
        SUB
    }
}
