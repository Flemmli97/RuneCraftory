package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.ServerValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class S2CSyncConfig implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_sync_config_values");

    private FriendlyByteBuf buf;

    public static S2CSyncConfig read(FriendlyByteBuf buf) {
        S2CSyncConfig pkt = new S2CSyncConfig();
        pkt.buf = new FriendlyByteBuf(buf.copy());
        return pkt;
    }

    public static void handle(S2CSyncConfig pkt) {
        int size = pkt.buf.readInt();
        Map<String, ServerValue<?>> configs = ServerValue.getSyncableValues();
        for (int i = 0; i < size; i++) {
            ServerValue<?> val = configs.get(pkt.buf.readUtf());
            if (val != null)
                val.readFromBuffer(pkt.buf);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        Map<String, ServerValue<?>> configs = ServerValue.getSyncableValues();
        buf.writeInt(configs.size());
        configs.forEach((key, val) -> {
            buf.writeUtf(key);
            val.writeToBuffer(buf);
        });
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
