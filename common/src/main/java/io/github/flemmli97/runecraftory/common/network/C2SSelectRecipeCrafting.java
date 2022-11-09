package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SSelectRecipeCrafting(int id) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_select_recipe_crafting");

    public static C2SSelectRecipeCrafting read(FriendlyByteBuf buf) {
        return new C2SSelectRecipeCrafting(buf.readInt());
    }

    public static void handle(C2SSelectRecipeCrafting pkt, ServerPlayer sender) {
        if (sender != null && sender.containerMenu instanceof ContainerCrafting crafting)
            crafting.updateCurrentRecipeIndex(pkt.id);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
