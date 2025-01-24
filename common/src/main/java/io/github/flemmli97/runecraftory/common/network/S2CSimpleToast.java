package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class S2CSimpleToast implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_simple_toast");

    private final Component title;
    private final Component subtitle;

    public S2CSimpleToast(Component title, Component subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public static S2CSimpleToast read(FriendlyByteBuf buf) {
        return new S2CSimpleToast(buf.readComponent(), buf.readComponent());
    }

    public static void handle(S2CSimpleToast pkt) {
        ClientHandlers.simpleToast(pkt.title, pkt.subtitle);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeComponent(this.title);
        buf.writeComponent(this.subtitle);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
