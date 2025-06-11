package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class S2CShopResponses implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CShopResponses> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_shop_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CShopResponses> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CShopResponses decode(RegistryFriendlyByteBuf buf) {
            if (buf.readBoolean())
                return new S2CShopResponses(ComponentSerialization.STREAM_CODEC.decode(buf));
            return new S2CShopResponses(null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CShopResponses pkt) {
            buf.writeBoolean(pkt.txt != null);
            if (pkt.txt != null)
                ComponentSerialization.STREAM_CODEC.encode(buf, pkt.txt);
        }
    };

    private final Component txt;

    public S2CShopResponses(Component txt) {
        this.txt = txt;
    }

    public static void handle(S2CShopResponses pkt) {
        ClientHandlers.handleShopRespone(pkt.txt);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
