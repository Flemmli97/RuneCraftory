package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SUpdateCraftingScreen(boolean increase) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_crafting_screen");

    public static C2SUpdateCraftingScreen read(FriendlyByteBuf buf) {
        return new C2SUpdateCraftingScreen(buf.readBoolean());
    }

    public static void handle(C2SUpdateCraftingScreen pkt, ServerPlayer sender) {
        if (sender != null && sender.containerMenu instanceof ContainerCrafting)
            if (pkt.increase)
                ((ContainerCrafting) sender.containerMenu).increase();
            else
                ((ContainerCrafting) sender.containerMenu).decrease();

    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.increase);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
