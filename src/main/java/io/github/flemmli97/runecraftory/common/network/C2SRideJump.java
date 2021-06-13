package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SRideJump {

    public C2SRideJump() {
    }

    public static C2SRideJump read(PacketBuffer buf) {
        return new C2SRideJump();
    }

    public static void write(C2SRideJump pkt, PacketBuffer buf) {
    }

    public static void handle(C2SRideJump pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.isPassenger() && player.getRidingEntity() instanceof BaseMonster)
                ((BaseMonster) player.getRidingEntity()).setDoJumping(true);
        });
        ctx.get().setPacketHandled(true);
    }
}
