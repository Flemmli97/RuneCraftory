package io.github.flemmli97.runecraftory.fabric.network;

import io.github.flemmli97.runecraftory.common.network.PacketRegistrar;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ServerPacketHandler {

    public static void registerServer() {
        PacketRegistrar.registerServerPackets(new PacketRegistrar.ServerPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> handler) {
                ServerPlayNetworking.registerGlobalReceiver(id, handlerServer(decoder, handler));
            }
        }, 0);
    }

    private static <T> ServerPlayNetworking.PlayChannelHandler handlerServer(Function<FriendlyByteBuf, T> decoder, BiConsumer<T, ServerPlayer> handler) {
        return (server, player, handler1, buf, responseSender) -> {
            T pkt = decoder.apply(buf);
            server.execute(() -> handler.accept(pkt, player));
        };
    }
}
