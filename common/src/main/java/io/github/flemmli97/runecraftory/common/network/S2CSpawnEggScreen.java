package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class S2CSpawnEggScreen implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSpawnEggScreen> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_spawn_egg_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSpawnEggScreen> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSpawnEggScreen decode(RegistryFriendlyByteBuf buf) {
            return new S2CSpawnEggScreen(buf.readEnum(InteractionHand.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSpawnEggScreen pkt) {
            buf.writeEnum(pkt.hand);
        }
    };

    private final InteractionHand hand;

    public S2CSpawnEggScreen(InteractionHand hand) {
        this.hand = hand;
    }

    public static void handle(S2CSpawnEggScreen pkt, Player player) {
        if (player == null)
            return;
        ClientHandlers.openSpawneggGui(pkt.hand);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}