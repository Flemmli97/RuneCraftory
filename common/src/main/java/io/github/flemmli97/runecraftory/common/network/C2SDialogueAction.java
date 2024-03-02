package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SDialogueAction implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_dialogue_action");

    private final int id, action;
    private final ConversationContext convCtx;
    private final String conversationID;

    public C2SDialogueAction(int entityID, ConversationContext convCtx, String conversationID, int action) {
        this.id = entityID;
        this.convCtx = convCtx;
        this.conversationID = conversationID;
        this.action = action;
    }

    public static C2SDialogueAction read(FriendlyByteBuf buf) {
        return new C2SDialogueAction(buf.readInt(), ConversationContext.get(buf.readResourceLocation()), buf.readUtf(), buf.readInt());
    }

    public static void handle(C2SDialogueAction pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof EntityNPCBase npc) {
                npc.handleDialogueAction(sender, pkt.convCtx, pkt.conversationID, pkt.action);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeResourceLocation(this.convCtx.key());
        buf.writeUtf(this.conversationID);
        buf.writeInt(this.action);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
