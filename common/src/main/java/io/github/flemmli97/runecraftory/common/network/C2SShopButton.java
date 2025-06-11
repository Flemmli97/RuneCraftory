package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record C2SShopButton(boolean next) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SShopButton> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_shop_buttons"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SShopButton> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SShopButton decode(RegistryFriendlyByteBuf buf) {
            return new C2SShopButton(buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SShopButton pkt) {
            buf.writeBoolean(pkt.next);
        }
    };

    public static void handle(C2SShopButton pkt, ServerPlayer sender) {
        AbstractContainerMenu c = sender.containerMenu;
        if (c instanceof ContainerShop shop) {
            if (pkt.next)
                shop.next();
            else
                shop.prev();
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CShopResponses(null), sender);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
