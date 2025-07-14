package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SDialogueAction implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SDialogueAction> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_dialogue_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SDialogueAction> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SDialogueAction decode(RegistryFriendlyByteBuf buf) {
            return new C2SDialogueAction(buf.readInt(), ConversationContext.get(buf.readResourceLocation()), buf.readUtf(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SDialogueAction pkt) {
            buf.writeInt(pkt.id);
            buf.writeResourceLocation(pkt.convCtx.key());
            buf.writeUtf(pkt.conversationID);
            buf.writeInt(pkt.action);
        }
    };

    private final int id, action;
    private final ConversationContext convCtx;
    private final String conversationID;

    public C2SDialogueAction(int entityID, ConversationContext convCtx, String conversationID, int action) {
        this.id = entityID;
        this.convCtx = convCtx;
        this.conversationID = conversationID;
        this.action = action;
    }

    public static void handle(C2SDialogueAction pkt, ServerPlayer sender) {
        Entity entity = sender.level().getEntity(pkt.id);
        if (entity instanceof NPCEntity npc) {
            npc.handleDialogueAction(sender, pkt.convCtx, pkt.conversationID, pkt.action);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
