package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CPlayerStats {

    private final float strength, intel, vit;

    private S2CPlayerStats(float strength, float intel, float vit) {
        this.strength = strength;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CPlayerStats(IPlayerCap cap) {
        this.strength = cap.getStr();
        this.intel = cap.getIntel();
        this.vit = cap.getVit();
    }

    public static S2CPlayerStats read(PacketBuffer buf) {
        return new S2CPlayerStats(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void write(S2CPlayerStats pkt, PacketBuffer buf) {
        buf.writeFloat(pkt.strength);
        buf.writeFloat(pkt.intel);
        buf.writeFloat(pkt.vit);
    }

    public static void handle(S2CPlayerStats pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.setStr(player, pkt.strength);
                cap.setIntel(player, pkt.intel);
                cap.setVit(player, pkt.vit);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
