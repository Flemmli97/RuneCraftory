package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CCalendar implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_calendar");

    private FriendlyByteBuf buffer;
    private CalendarImpl calendar;

    private S2CCalendar(FriendlyByteBuf buf) {
        this.buffer = buf;
    }

    public S2CCalendar(CalendarImpl calendar) {
        this.calendar = calendar;
    }

    public static S2CCalendar read(FriendlyByteBuf buf) {
        return new S2CCalendar(new FriendlyByteBuf(buf.copy()));
    }

    public static void handle(S2CCalendar pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        ClientHandlers.updateClientCalendar(pkt.buffer);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.calendar.toPacket(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
