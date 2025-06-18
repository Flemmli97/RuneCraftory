package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class S2CSimpleToast implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSimpleToast> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_simple_toast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSimpleToast> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSimpleToast decode(RegistryFriendlyByteBuf buf) {
            return new S2CSimpleToast(ComponentSerialization.STREAM_CODEC.decode(buf), ComponentSerialization.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSimpleToast pkt) {
            ComponentSerialization.STREAM_CODEC.encode(buf, pkt.title);
            ComponentSerialization.STREAM_CODEC.encode(buf, pkt.subtitle);
        }
    };

    private final Component title;
    private final Component subtitle;

    public S2CSimpleToast(Component title, Component subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public static void handle(S2CSimpleToast pkt) {
        ClientHandlers.simpleToast(pkt.title, pkt.subtitle);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
