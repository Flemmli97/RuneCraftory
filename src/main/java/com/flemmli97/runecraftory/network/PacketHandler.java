package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final SimpleChannel dispatcher =
            NetworkRegistry.ChannelBuilder.named(new ResourceLocation(RuneCraftory.MODID, "packets"))
                    .clientAcceptedVersions(a -> true)
                    .serverAcceptedVersions(a -> true)
                    .networkProtocolVersion(() -> "v1.0").simpleChannel();

    public static void register() {
        int id = 0;
        dispatcher.registerMessage(id++, S2CAttackDebug.class, S2CAttackDebug::write, S2CAttackDebug::read, S2CAttackDebug::handle);
        dispatcher.registerMessage(id++, C2SRideJump.class, C2SRideJump::write, C2SRideJump::read, C2SRideJump::handle);
        dispatcher.registerMessage(id++, S2CDataPackSync.class, S2CDataPackSync::write, S2CDataPackSync::read, S2CDataPackSync::handle);
        dispatcher.registerMessage(id++, S2CMaxRunePoints.class, S2CMaxRunePoints::write, S2CMaxRunePoints::read, S2CMaxRunePoints::handle);
        dispatcher.registerMessage(id++, S2CMoney.class, S2CMoney::write, S2CMoney::read, S2CMoney::handle);
        dispatcher.registerMessage(id++, S2CRunePoints.class, S2CRunePoints::write, S2CRunePoints::read, S2CRunePoints::handle);

    }

    public static <T> void sendToClient(T message, ServerPlayerEntity player) {
        dispatcher.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T message) {
        dispatcher.sendToServer(message);
    }

    public static <T> void sendToAll(T message) {
        dispatcher.send(PacketDistributor.ALL.noArg(), message);
    }
}
