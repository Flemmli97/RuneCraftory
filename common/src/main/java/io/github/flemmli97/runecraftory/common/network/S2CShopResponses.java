package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class S2CShopResponses implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_shop_response");

    private final Component txt;

    public S2CShopResponses(Component txt) {
        this.txt = txt;
    }

    public static S2CShopResponses read(FriendlyByteBuf buf) {
        if (buf.readBoolean())
            return new S2CShopResponses(buf.readComponent());
        return new S2CShopResponses(null);
    }

    public static void handle(S2CShopResponses pkt) {
        ClientHandlers.handleShopRespone(pkt.txt);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.txt != null);
        if (this.txt != null)
            buf.writeComponent(this.txt);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
