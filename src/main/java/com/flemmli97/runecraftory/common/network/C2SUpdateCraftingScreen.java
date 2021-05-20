package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SUpdateCraftingScreen {

    private final boolean increase;

    public C2SUpdateCraftingScreen(boolean increase) {
        this.increase = increase;
    }

    public static C2SUpdateCraftingScreen read(PacketBuffer buf) {
        return new C2SUpdateCraftingScreen(buf.readBoolean());
    }

    public static void write(C2SUpdateCraftingScreen pkt, PacketBuffer buf) {
        buf.writeBoolean(pkt.increase);
    }

    public static void handle(C2SUpdateCraftingScreen pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.openContainer instanceof ContainerCrafting)
                if (pkt.increase)
                    ((ContainerCrafting) player.openContainer).increase();
                else
                    ((ContainerCrafting) player.openContainer).decrease();

        });
        ctx.get().setPacketHandled(true);
    }
}
