package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CRuneyWeatherData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_weather");

    private final boolean runeyWeather;

    public S2CRuneyWeatherData(boolean runeyWeather) {
        this.runeyWeather = runeyWeather;
    }

    public static S2CRuneyWeatherData read(FriendlyByteBuf buf) {
        return new S2CRuneyWeatherData(buf.readBoolean());
    }

    public static void handle(S2CRuneyWeatherData pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        ClientHandlers.setRuneyWeather(pkt.runeyWeather);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.runeyWeather);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
