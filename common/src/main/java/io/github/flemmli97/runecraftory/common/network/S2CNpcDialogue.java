package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record S2CNpcDialogue(int entity, ConversationContext convCtx, String conversationID, Component component,
                             List<Component> actions) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_npc_dialogue");

    public static S2CNpcDialogue read(FriendlyByteBuf buf) {
        return new S2CNpcDialogue(buf.readInt(), buf.readBoolean() ? ConversationContext.get(buf.readResourceLocation()) : null,
                buf.readBoolean() ? buf.readUtf() : null, buf.readComponent(), buf.readList(FriendlyByteBuf::readComponent));
    }

    public static void handle(S2CNpcDialogue pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity entity = player.level.getEntity(pkt.entity);
        if (entity instanceof EntityNPCBase npc)
            ClientHandlers.updateNPCDialogue(npc, pkt.convCtx, pkt.conversationID, pkt.component, pkt.actions);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity);
        buf.writeBoolean(this.convCtx != null);
        if (this.convCtx != null)
            buf.writeResourceLocation(this.convCtx.key());
        buf.writeBoolean(this.conversationID != null);
        if (this.conversationID != null)
            buf.writeUtf(this.conversationID);
        buf.writeComponent(this.component);
        buf.writeCollection(this.actions, FriendlyByteBuf::writeComponent);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
