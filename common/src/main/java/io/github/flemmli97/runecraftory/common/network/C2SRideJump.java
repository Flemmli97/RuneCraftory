package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class C2SRideJump implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SRideJump> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_ride_jump"));

    public static final C2SRideJump INSTANCE = new C2SRideJump();
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SRideJump> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private C2SRideJump() {
    }

    public static void handle(C2SRideJump pkt, ServerPlayer sender) {
        if (sender.isPassenger() && sender.getVehicle() instanceof BaseMonster)
            ((BaseMonster) sender.getVehicle()).setDoJumping(true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
