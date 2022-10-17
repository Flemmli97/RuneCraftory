package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record C2SShopButton(boolean next) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_shop_buttons");

    public static C2SShopButton read(FriendlyByteBuf buf) {
        return new C2SShopButton(buf.readBoolean());
    }

    public static void handle(C2SShopButton pkt, ServerPlayer sender) {
        if (sender != null) {
            AbstractContainerMenu c = sender.containerMenu;
            if (c instanceof ContainerShop shop) {
                if (pkt.next)
                    shop.next();
                else
                    shop.prev();
                Platform.INSTANCE.sendToClient(new S2CShopResponses(null), sender);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.next);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
