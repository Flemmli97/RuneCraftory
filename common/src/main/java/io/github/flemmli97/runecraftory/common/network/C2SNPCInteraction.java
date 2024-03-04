package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;

public class C2SNPCInteraction implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_npc_interaction");

    private final int id;
    private final C2SNPCInteraction.Type type;
    private final String action;

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Type type) {
        this(entityID, type, "");
    }

    public C2SNPCInteraction(int entityID, String action) {
        this(entityID, Type.ACTION, action);
    }

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Type type, String action) {
        this.id = entityID;
        this.type = type;
        this.action = action == null ? "" : action;
    }

    public static C2SNPCInteraction read(FriendlyByteBuf buf) {
        return new C2SNPCInteraction(buf.readInt(), buf.readEnum(C2SNPCInteraction.Type.class), buf.readUtf());
    }

    public static void handle(C2SNPCInteraction pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof EntityNPCBase npc) {
                switch (pkt.type) {
                    case TALK -> npc.talkTo(sender);
                    case FOLLOW -> {
                        if (Platform.INSTANCE.getPlayerData(sender).map(d -> !d.party.isPartyMember(entity) && d.party.isPartyFull()).orElse(true)) {
                            sender.sendMessage(new TranslatableComponent("runecraftory.monster.interact.party.full"), Util.NIL_UUID);
                            return;
                        }
                        if (npc.getEntityToFollowUUID() == null)
                            npc.followEntity(sender);
                    }
                    case FOLLOWDISTANCE -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.setBehaviour(EntityNPCBase.Behaviour.FOLLOW_DISTANCE);
                    }
                    case STAY -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.setBehaviour(EntityNPCBase.Behaviour.STAY);
                    }
                    case STOPFOLLOW -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.followEntity(null);
                    }
                    case SHOP -> npc.openShopForPlayer(sender);
                    case QUEST -> npc.respondToQuest(sender, new ResourceLocation(pkt.action));
                    case CLOSE -> npc.closedDialogue(sender);
                    case CLOSE_QUEST -> npc.closedQuestDialogue(sender);
                    case ACTION -> npc.getShop().handleAction(npc, sender, pkt.action);
                }
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeEnum(this.type);
        buf.writeUtf(this.action);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {

        TALK("runecraftory.gui.npc.talk", null),
        FOLLOW("runecraftory.gui.npc.follow", EntityNPCBase.Behaviour.FOLLOW),
        FOLLOWDISTANCE("runecraftory.gui.npc.distance", EntityNPCBase.Behaviour.FOLLOW_DISTANCE),
        STAY("runecraftory.gui.npc.stay", EntityNPCBase.Behaviour.STAY),
        STOPFOLLOW("runecraftory.gui.npc.stopFollow", EntityNPCBase.Behaviour.WANDER),
        SHOP("runecraftory.gui.npc.shop", null),
        CLOSE("runecraftory.gui.npc.close", null),
        CLOSE_QUEST("runecraftory.gui.npc.close.quest", null),
        QUEST(SimpleQuestIntegration.QUEST_GUI_KEY, null),
        ACTION("", null);

        public final String translation;
        @Nullable
        public final EntityNPCBase.Behaviour behaviour;

        Type(String translation, EntityNPCBase.Behaviour behaviour) {
            this.translation = translation;
            this.behaviour = behaviour;
        }
    }
}
