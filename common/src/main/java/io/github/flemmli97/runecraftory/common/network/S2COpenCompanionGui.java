package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class S2COpenCompanionGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_companion_gui");

    private final int entityID;
    private final boolean fullParty, hasHome;

    private S2COpenCompanionGui(int id, boolean fullParty, boolean hasHome) {
        this.entityID = id;
        this.fullParty = fullParty;
        this.hasHome = hasHome;
    }

    public S2COpenCompanionGui(BaseMonster entity, ServerPlayer player) {
        this.entityID = entity.getId();
        this.fullParty = Platform.INSTANCE.getPlayerData(player)
                .map(d -> d.party.isPartyFull()).orElse(true);
        this.hasHome = entity.getAssignedBarn() != null;
    }

    public static S2COpenCompanionGui read(FriendlyByteBuf buf) {
        return new S2COpenCompanionGui(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(S2COpenCompanionGui pkt) {
        ClientHandlers.openCompanionGui(pkt.entityID, pkt.fullParty, pkt.hasHome);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.fullParty);
        buf.writeBoolean(this.hasHome);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
