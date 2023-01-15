package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class C2SNPCInteraction implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_npc_interaction");

    private final int id;
    private final C2SNPCInteraction.Type type;
    private final String action;

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Type type) {
        this.id = entityID;
        this.type = type;
        this.action = "";
    }

    public C2SNPCInteraction(int entityID, String action) {
        this.id = entityID;
        this.type = Type.ACTION;
        this.action = action;
    }

    public static C2SNPCInteraction read(FriendlyByteBuf buf) {
        int id = buf.readInt();
        Type type = buf.readEnum(C2SNPCInteraction.Type.class);
        if (type != Type.ACTION)
            return new C2SNPCInteraction(id, type);
        return new C2SNPCInteraction(id, buf.readUtf());
    }

    public static void handle(C2SNPCInteraction pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof EntityNPCBase npc) {
                npc.decreaseInteractingPlayers(sender);
                switch (pkt.type) {
                    case TALK -> npc.talkTo(sender);
                    case FOLLOW -> {
                        if (Platform.INSTANCE.getPlayerData(sender).map(d -> !d.party.isPartyMember(entity) && d.party.isPartyFull()).orElse(true)) {
                            sender.sendMessage(new TranslatableComponent("monster.interact.party.full"), Util.NIL_UUID);
                            return;
                        }
                        if (npc.getEntityToFollowUUID() == null)
                            npc.followEntity(sender);
                    }
                    case FOLLOWDISTANCE -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.followAtDistance(sender);
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
                    case ACTION -> npc.getShop().handleAction(npc, sender, pkt.action);
                }
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeEnum(this.type);
        if (this.type == Type.ACTION)
            buf.writeUtf(this.action);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {

        TALK("gui.npc.talk", null),
        FOLLOW("gui.npc.follow", EntityNPCBase.Behaviour.FOLLOW),
        FOLLOWDISTANCE("gui.npc.distance", EntityNPCBase.Behaviour.FOLLOW_DISTANCE),
        STAY("gui.npc.stay", EntityNPCBase.Behaviour.STAY),
        STOPFOLLOW("gui.npc.stopFollow", EntityNPCBase.Behaviour.WANDER),
        SHOP("gui.npc.shop", null),
        CLOSE("gui.npc.close", null),
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
