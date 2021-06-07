package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class C2SOpenInfo {

    private final Type type;

    public C2SOpenInfo(Type type) {
        this.type = type;
    }

    public static C2SOpenInfo read(PacketBuffer buf) {
        return new C2SOpenInfo(buf.readEnumValue(Type.class));
    }

    public static void write(C2SOpenInfo pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.type);
    }

    public static void handle(C2SOpenInfo pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                switch (pkt.type) {
                    case MAIN:
                        PacketHandler.sendToClient(new S2CCapSync(player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"))), player);
                        NetworkHooks.openGui(player, ContainerInfoScreen.create());
                        break;
                    case SUB:
                        PacketHandler.sendToClient(new S2CCapSync(player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"))), player);
                        NetworkHooks.openGui(player, ContainerInfoScreen.createSub());
                        break;
                    case INV:
                        ItemStack stack = player.inventory.getItemStack();
                        player.inventory.setItemStack(ItemStack.EMPTY);
                        player.closeContainer();
                        if(!stack.isEmpty()){
                            player.inventory.setItemStack(stack);
                            player.connection.sendPacket(new SSetSlotPacket(-1, -1, stack));
                        }
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type {
        MAIN,
        SUB,
        INV
    }
}
