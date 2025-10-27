package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.ShopState;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.world.data.family.SyncedFamilyData;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class S2COpenNPCGui implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2COpenNPCGui> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_npc_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenNPCGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2COpenNPCGui decode(RegistryFriendlyByteBuf buf) {
            return new S2COpenNPCGui(buf.readInt(), buf.readEnum(ShopState.class), buf.readInt(),
                    buf.readMap(LinkedHashMap::new, ByteBufCodecs.STRING_UTF8, buf1 -> buf1.readList(b -> ComponentSerialization.STREAM_CODEC.decode(buf))), !buf.readBoolean() ? null : buf.readResourceLocation(),
                    SyncedFamilyData.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2COpenNPCGui pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeEnum(pkt.isShopOpen);
            buf.writeInt(pkt.followState);
            buf.writeMap(pkt.actions, ByteBufCodecs.STRING_UTF8, (buf1, components) -> buf1.writeCollection(components, (b, c) -> ComponentSerialization.STREAM_CODEC.encode(buf, c)));
            buf.writeBoolean(pkt.quest != null);
            if (pkt.quest != null)
                buf.writeResourceLocation(pkt.quest);
            SyncedFamilyData.STREAM_CODEC.encode(buf, pkt.family);
        }
    };

    private final int entityID;
    private final ShopState isShopOpen;
    private final int followState;
    private final Map<String, List<Component>> actions;
    private final ResourceLocation quest;
    private final SyncedFamilyData family;

    private S2COpenNPCGui(int id, ShopState isShopOpen, int followState, Map<String, List<Component>> actions, ResourceLocation quest, SyncedFamilyData family) {
        this.entityID = id;
        this.isShopOpen = isShopOpen;
        this.followState = followState;
        this.actions = actions;
        this.quest = quest;
        this.family = family;
    }

    public S2COpenNPCGui(NPCEntity entity, ServerPlayer player) {
        this.entityID = entity.getId();
        this.isShopOpen = entity.canTrade();
        this.actions = entity.getProfession().actions(entity, player);
        this.quest = QuestHandler.questForExists(player, entity);
        if (entity.getEntityToFollowUUID() == null)
            this.followState = RunecraftoryAttachments.PLAYER_DATA.get().get(player).party.isPartyFull() ? 2 : 0;
        else
            this.followState = entity.getEntityToFollowUUID().equals(player.getUUID()) ? 1 : 2;
        this.family = entity.getFamily().forSyncing(entity, player);
    }

    public static void handle(S2COpenNPCGui pkt) {
        ClientHandlers.openNPCChat(pkt.entityID, pkt.isShopOpen, pkt.family, pkt.followState, pkt.actions, pkt.quest);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
