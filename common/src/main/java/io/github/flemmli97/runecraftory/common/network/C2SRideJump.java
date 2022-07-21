package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2SRideJump implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_ride_jump");

    public static C2SRideJump read(FriendlyByteBuf buf) {
        return new C2SRideJump();
    }

    public static void handle(C2SRideJump pkt, ServerPlayer sender) {
        if (sender != null && sender.isPassenger() && sender.getVehicle() instanceof BaseMonster)
            ((BaseMonster) sender.getVehicle()).setDoJumping(true);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
