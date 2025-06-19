package io.github.flemmli97.runecraftory.neoforge.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.network.PacketRegistrar;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;

public class PacketHandler {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(RuneCraftory.MODID).optional();
        PacketRegistrar.registerServerPackets(new PacketRegistrar.ServerPacketRegister() {
            @Override
            public <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler) {
                registrar.playToServer(type, codec, (pkt, ctx) -> ctx.enqueueWork(() -> handler.accept(pkt, (ServerPlayer) ctx.player())));
            }
        });
        PacketRegistrar.registerClientPackets(new PacketRegistrar.ClientPacketRegister() {
            @Override
            public <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, Player> handler) {
                registrar.playToClient(type, codec, (pkt, ctx) -> ctx.enqueueWork(() -> handler.accept(pkt, ctx.player())));
            }
        });
    }
}
