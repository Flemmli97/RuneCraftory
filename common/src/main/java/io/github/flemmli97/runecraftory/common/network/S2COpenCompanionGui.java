package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2COpenCompanionGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_companion_gui");

    private final int entityID;

    private S2COpenCompanionGui(int id) {
        this.entityID = id;
    }

    public S2COpenCompanionGui(BaseMonster entity) {
        this.entityID = entity.getId();
    }

    public static S2COpenCompanionGui read(FriendlyByteBuf buf) {
        return new S2COpenCompanionGui(buf.readInt());
    }

    public static void handle(S2COpenCompanionGui pkt) {
        ClientHandlers.openCompanionGui(pkt.entityID);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
