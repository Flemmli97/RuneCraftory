package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class S2CCalendar implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CCalendar> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_calendar"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CCalendar> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CCalendar decode(RegistryFriendlyByteBuf buf) {
            return new S2CCalendar(CalendarImpl.CalendarSync.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CCalendar pkt) {
            CalendarImpl.CalendarSync.STREAM_CODEC.encode(buf, pkt.data);
        }
    };

    private final CalendarImpl.CalendarSync data;

    private S2CCalendar(CalendarImpl.CalendarSync data) {
        this.data = data;
    }

    public S2CCalendar(CalendarImpl calendar) {
        this.data = calendar.getData();
    }

    public static void handle(S2CCalendar pkt) {
        ClientHandlers.updateClientCalendar(pkt.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
