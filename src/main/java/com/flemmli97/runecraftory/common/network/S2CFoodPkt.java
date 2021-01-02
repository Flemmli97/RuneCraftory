package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class S2CFoodPkt {

    private final ItemStack stack;

    public S2CFoodPkt(@Nullable ItemStack stack) {
        this.stack = stack;
    }

    public static S2CFoodPkt read(PacketBuffer buf) {
        if (buf.readBoolean())
            return new S2CFoodPkt(buf.readItemStack());
        return new S2CFoodPkt(null);
    }

    public static void write(S2CFoodPkt pkt, PacketBuffer buf) {
        buf.writeBoolean(pkt.stack != null);
        if (pkt.stack != null)
            buf.writeItemStack(pkt.stack);
    }

    public static void handle(S2CFoodPkt pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                if (pkt.stack == null)
                    cap.removeFoodEffect(player);
                else
                    cap.applyFoodEffect(player, pkt.stack);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
