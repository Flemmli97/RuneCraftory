package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CCalendar {

    private PacketBuffer buffer;
    private CalendarImpl calendar;

    private S2CCalendar(PacketBuffer buf) {
        this.buffer = buf;
    }

    public S2CCalendar(CalendarImpl calendar) {
        this.calendar = calendar;
    }

    public static S2CCalendar read(PacketBuffer buf) {
        return new S2CCalendar(buf);
    }

    public static void write(S2CCalendar pkt, PacketBuffer buf) {
        pkt.calendar.toPacket(buf);
    }

    public static void handle(S2CCalendar pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            ClientHandlers.updateClientCalendar(pkt.buffer);
        });
        ctx.get().setPacketHandled(true);
    }
}
