package io.github.flemmli97.runecraftory.fabric.network;

import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.common.network.PacketRegistrar;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientPacketHandler {

    public static void registerClientPackets() {
        PacketRegistrar.registerClientPackets(new PacketRegistrar.ClientPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler) {
                ClientPlayNetworking.registerGlobalReceiver(id, handlerClient(decoder, handler));
            }
        }, 0);
    }

    public static void sendToServer(Packet packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        ClientPlayNetworking.send(packet.getID(), buf);
    }

    private static <T> ClientPlayNetworking.PlayChannelHandler handlerClient(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        return (client, handler1, buf, responseSender) -> {
            T pkt = decoder.apply(buf);
            client.execute(() -> handler.accept(pkt));
        };
    }
}
