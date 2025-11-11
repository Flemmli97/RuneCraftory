package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class S2COpenCompanionGui implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2COpenCompanionGui> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_companion_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenCompanionGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2COpenCompanionGui decode(RegistryFriendlyByteBuf buf) {
            return new S2COpenCompanionGui(buf.readInt(), buf.readBoolean(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2COpenCompanionGui pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeBoolean(pkt.fullParty);
            buf.writeBoolean(pkt.hasHome);
        }
    };

    private final int entityID;
    private final boolean fullParty, hasHome;

    private S2COpenCompanionGui(int id, boolean fullParty, boolean hasHome) {
        this.entityID = id;
        this.fullParty = fullParty;
        this.hasHome = hasHome;
    }

    public S2COpenCompanionGui(BaseMonster entity, ServerPlayer player) {
        this.entityID = entity.getId();
        this.fullParty = RunecraftoryAttachments.PLAYER_DATA.get().get(player).party.isPartyFull();
        this.hasHome = entity.getAssignedBarn() != null;
    }

    public static void handle(S2COpenCompanionGui pkt) {
        ClientHandlers.openCompanionGui(pkt.entityID, pkt.fullParty, pkt.hasHome);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
