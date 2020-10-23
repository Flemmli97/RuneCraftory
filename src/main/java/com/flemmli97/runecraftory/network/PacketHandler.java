package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.network.C2SRideJump;
import com.flemmli97.runecraftory.mobs.network.S2CAttackDebug;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final SimpleChannel dispatcher =
            NetworkRegistry.ChannelBuilder.named(new ResourceLocation(RuneCraftory.MODID, "packets"))
                    .clientAcceptedVersions(a -> true)
                    .serverAcceptedVersions(a -> true)
                    .networkProtocolVersion(() -> "v1.0").simpleChannel();
    private static int id = 0;

    public static <MSG> void register(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        dispatcher.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    public static void register() {
        register(S2CAttackDebug.class, S2CAttackDebug::write, S2CAttackDebug::read, S2CAttackDebug::handle);
        register(C2SRideJump.class, C2SRideJump::write, C2SRideJump::read, C2SRideJump::handle);
    }

    public static <T> void sendToClient(T message, ServerPlayerEntity player) {
        dispatcher.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToAll(T message) {
        dispatcher.send(PacketDistributor.ALL.noArg(), message);
    }
}
