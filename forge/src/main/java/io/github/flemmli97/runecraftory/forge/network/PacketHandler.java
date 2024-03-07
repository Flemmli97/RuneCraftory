package io.github.flemmli97.runecraftory.forge.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.network.PacketRegistrar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final SimpleChannel DISPATCHER =
            NetworkRegistry.ChannelBuilder.named(new ResourceLocation(RuneCraftory.MODID, "packets"))
                    .clientAcceptedVersions(a -> true)
                    .serverAcceptedVersions(a -> true)
                    .networkProtocolVersion(() -> "v1.0").simpleChannel();

    public static void register() {
        int server = PacketRegistrar.registerServerPackets(new PacketRegistrar.ServerPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> handler) {
                DISPATCHER.registerMessage(index, clss, encoder, decoder, handlerServer(handler), Optional.of(NetworkDirection.PLAY_TO_SERVER));
            }
        }, 0);
        PacketRegistrar.registerClientPackets(new PacketRegistrar.ClientPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler) {
                DISPATCHER.registerMessage(index, clss, encoder, decoder, handlerClient(handler), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
            }
        }, server);
    }

    public static <T> void sendToClient(T message, ServerPlayer player) {
        DISPATCHER.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T message) {
        DISPATCHER.sendToServer(message);
    }

    public static <T> void sendToAll(T message) {
        DISPATCHER.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <T> void sendToTrackingAndSelf(T message, Entity e) {
        DISPATCHER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e), message);
    }

    public static <T> void sendToTracking(T message, ServerLevel level, ChunkPos pos) {
        ChunkAccess chunk = level.getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk instanceof LevelChunk levelChunk)
            DISPATCHER.send(PacketDistributor.TRACKING_CHUNK.with(() -> levelChunk), message);
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> handlerServer(BiConsumer<T, ServerPlayer> handler) {
        return (p, ctx) -> {
            ctx.get().enqueueWork(() -> handler.accept(p, ctx.get().getSender()));
            ctx.get().setPacketHandled(true);
        };
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> handlerClient(Consumer<T> handler) {
        return (p, ctx) -> {
            ctx.get().enqueueWork(() -> handler.accept(p));
            ctx.get().setPacketHandled(true);
        };
    }
}
