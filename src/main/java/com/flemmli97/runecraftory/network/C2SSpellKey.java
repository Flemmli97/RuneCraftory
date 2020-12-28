package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSpellKey {

    private final int num;

    public C2SSpellKey(int num) {
        this.num = num;
    }

    public static C2SSpellKey read(PacketBuffer buf) {
        return new C2SSpellKey(buf.readInt());
    }

    public static void write(C2SSpellKey pkt, PacketBuffer buf) {
        buf.writeInt(pkt.num);
    }

    public static void handle(C2SSpellKey pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.getInv().useSkill(player, pkt.num));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
