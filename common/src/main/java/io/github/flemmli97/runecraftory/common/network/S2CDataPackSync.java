package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CDataPackSync implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_datapack_sync");

    private FriendlyByteBuf buffer;

    private final SyncedType type;

    private S2CDataPackSync(FriendlyByteBuf buf) {
        this.type = buf.readEnum(SyncedType.class);
        this.buffer = buf;
    }

    public S2CDataPackSync(SyncedType type) {
        this.type = type;
    }

    public static S2CDataPackSync read(FriendlyByteBuf buf) {
        return new S2CDataPackSync(new FriendlyByteBuf(buf.copy()));
    }

    public static void handle(S2CDataPackSync pkt) {
        DataPackHandler.fromPacket(pkt.type, pkt.buffer);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        DataPackHandler.toPacket(buf, this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum SyncedType {
        ITEMSTATS,
        CROPS,
        FOOD,
    }
}
