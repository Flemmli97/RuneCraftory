package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SNPCInteraction implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_npc_interaction");

    private final int id;
    private final C2SNPCInteraction.Type type;

    public C2SNPCInteraction(int entityID, C2SNPCInteraction.Type type) {
        this.id = entityID;
        this.type = type;
    }

    public static C2SNPCInteraction read(FriendlyByteBuf buf) {
        return new C2SNPCInteraction(buf.readInt(), buf.readEnum(C2SNPCInteraction.Type.class));
    }

    public static void handle(C2SNPCInteraction pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof EntityNPCBase npc) {
                switch (pkt.type) {
                    case TALK -> npc.talkTo(sender);
                    case FOLLOW -> {
                        if (npc.getEntityToFollowUUID() == null)
                            npc.followEntity(sender);
                    }
                    case FOLLOWDISTANCE -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.followEntity(sender);
                    }
                    case STAY -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.stayHere(true);
                    }
                    case STOPFOLLOW -> {
                        if (npc.getEntityToFollowUUID() != null && npc.getEntityToFollowUUID().equals(sender.getUUID()))
                            npc.followEntity(null);
                    }
                    case SHOP -> npc.openShopForPlayer(sender);
                }
                npc.decreaseInteractingPlayers(sender);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeEnum(this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        TALK,
        FOLLOW,
        FOLLOWDISTANCE,
        STAY,
        STOPFOLLOW,
        SHOP,
        CLOSE
    }
}
