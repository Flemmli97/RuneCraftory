package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CMoney {

    private final int money;

    private S2CMoney(int money) {
        this.money = money;
    }

    public S2CMoney(IPlayerCap cap) {
        this.money = cap.getMoney();
    }

    public static S2CMoney read(PacketBuffer buf) {
        return new S2CMoney(buf.readInt());
    }

    public static void write(S2CMoney pkt, PacketBuffer buf) {
        buf.writeInt(pkt.money);
    }

    public static void handle(S2CMoney pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> cap.setMoney(player, pkt.money));
        });
        ctx.get().setPacketHandled(true);
    }
}
