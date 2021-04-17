package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SRideAttack {

    private int command;

    public C2SRideAttack(int command) {
        this.command = command;
    }

    public static C2SRideAttack read(PacketBuffer buf) {
        return new C2SRideAttack(buf.readInt());
    }

    public static void write(C2SRideAttack pkt, PacketBuffer buf) {
        buf.writeInt(pkt.command);
    }

    public static void handle(C2SRideAttack pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.isPassenger() && player.getRidingEntity() instanceof BaseMonster)
                ((BaseMonster) player.getRidingEntity()).handleRidingCommand(pkt.command);
        });
        ctx.get().setPacketHandled(true);
    }
}
