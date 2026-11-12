package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class S2CSpawnEggScreen implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSpawnEggScreen> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_spawn_egg_screen"));

    private static final StreamCodec<ByteBuf, Optional<List<ResourceLocation>>> ID_LIST =
            ByteBufCodecs.optional(ByteBufCodecs.collection(ArrayList::new, ResourceLocation.STREAM_CODEC.cast()));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSpawnEggScreen> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSpawnEggScreen decode(RegistryFriendlyByteBuf buf) {
            return new S2CSpawnEggScreen(buf.readEnum(InteractionHand.class), ID_LIST.decode(buf).orElse(null));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSpawnEggScreen pkt) {
            buf.writeEnum(pkt.hand);
            ID_LIST.encode(buf, Optional.ofNullable(pkt.npcIds));
        }
    };

    private final InteractionHand hand;
    private final List<ResourceLocation> npcIds;

    public S2CSpawnEggScreen(InteractionHand hand) {
        this(hand, null);
    }

    public S2CSpawnEggScreen(InteractionHand hand, List<ResourceLocation> npcIds) {
        this.hand = hand;
        this.npcIds = npcIds;
    }

    public static void handle(S2CSpawnEggScreen pkt, Player player) {
        if (player == null)
            return;
        ClientHandlers.openSpawneggGui(pkt.hand, pkt.npcIds);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}