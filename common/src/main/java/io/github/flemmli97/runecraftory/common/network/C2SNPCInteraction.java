package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class C2SNPCInteraction implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SNPCInteraction> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_npc_interaction"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SNPCInteraction> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SNPCInteraction decode(RegistryFriendlyByteBuf buf) {
            return new C2SNPCInteraction(buf.readInt(), buf.readEnum(C2SNPCInteraction.Action.class), buf.readUtf());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SNPCInteraction pkt) {
            buf.writeInt(pkt.id);
            buf.writeEnum(pkt.type);
            buf.writeUtf(pkt.action);
        }
    };

    private final int id;
    private final C2SNPCInteraction.Action type;
    private final String action;

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Action type) {
        this(entityID, type, "");
    }

    public C2SNPCInteraction(int entityID, String action) {
        this(entityID, Action.ACTION, action);
    }

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Action type, String action) {
        this.id = entityID;
        this.type = type;
        this.action = action == null ? "" : action;
    }

    public static void handle(C2SNPCInteraction pkt, ServerPlayer sender) {
        Entity entity = sender.level().getEntity(pkt.id);
        if (entity instanceof NPCEntity npc) {
            switch (pkt.type) {
                case TALK -> npc.talkTo(sender);
                case FOLLOW -> {
                    PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(sender);
                    if (!data.party.isPartyMember(entity) && data.party.isPartyFull()) {
                        sender.displayClientMessage(Component.translatable("runecraftory.monster.interact.party.full"), true);
                        return;
                    }
                    if (npc.getEntityToFollowUUID() == null)
                        npc.followEntity(sender);
                }
                case FOLLOWDISTANCE -> {
                    if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                        npc.setBehaviour(NPCEntity.Behaviour.FOLLOW_DISTANCE);
                }
                case STAY -> {
                    if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                        npc.setBehaviour(NPCEntity.Behaviour.STAY);
                }
                case STOPFOLLOW -> {
                    if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                        npc.followEntity(null);
                }
                case SHOP -> npc.openShopForPlayer(sender);
                case QUEST -> npc.respondToQuest(sender, ResourceLocation.parse(pkt.action));
                case CLOSE -> npc.closedDialogue(sender);
                case CLOSE_QUEST -> npc.closedQuestDialogue(sender);
                case ACTION -> {
                    if (!npc.isBaby())
                        npc.getProfession().handleAction(npc, sender, pkt.action);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {

        TALK("runecraftory.gui.npc.talk", null),
        FOLLOW("runecraftory.gui.npc.follow", NPCEntity.Behaviour.FOLLOW),
        FOLLOWDISTANCE("runecraftory.gui.npc.distance", NPCEntity.Behaviour.FOLLOW_DISTANCE),
        STAY("runecraftory.gui.npc.stay", NPCEntity.Behaviour.STAY),
        STOPFOLLOW("runecraftory.gui.npc.stopFollow", NPCEntity.Behaviour.WANDER),
        SHOP("runecraftory.gui.npc.shop", null),
        CLOSE("runecraftory.gui.npc.close", null),
        CLOSE_QUEST("runecraftory.gui.npc.close.quest", null),
        QUEST("runecraftory.gui.quest.button", null),
        ACTION("", null);

        public final String translation;
        @Nullable
        public final NPCEntity.Behaviour behaviour;

        Action(String translation, NPCEntity.Behaviour behaviour) {
            this.translation = translation;
            this.behaviour = behaviour;
        }
    }
}
