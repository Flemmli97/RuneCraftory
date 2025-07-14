package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public record S2CNpcDialogue(int entity, ConversationContext convCtx, String conversationID, Component component,
                             Map<String, Component> data,
                             List<Component> actions) implements CustomPacketPayload {

    public static final ResourceLocation ID = RuneCraftory.modRes("s2c_npc_dialogue");
    public static final CustomPacketPayload.Type<S2CNpcDialogue> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_npc_dialogue"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CNpcDialogue> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CNpcDialogue decode(RegistryFriendlyByteBuf buf) {
            return new S2CNpcDialogue(buf.readInt(), buf.readBoolean() ? ConversationContext.get(buf.readResourceLocation()) : null,
                    buf.readBoolean() ? buf.readUtf() : null, ComponentSerialization.STREAM_CODEC.decode(buf),
                    buf.readMap(ByteBufCodecs.STRING_UTF8, b -> ComponentSerialization.STREAM_CODEC.decode(buf)),
                    buf.readList(b -> ComponentSerialization.STREAM_CODEC.decode(buf)));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CNpcDialogue pkt) {
            buf.writeInt(pkt.entity);
            buf.writeBoolean(pkt.convCtx != null);
            if (pkt.convCtx != null)
                buf.writeResourceLocation(pkt.convCtx.key());
            buf.writeBoolean(pkt.conversationID != null);
            if (pkt.conversationID != null)
                buf.writeUtf(pkt.conversationID);
            ComponentSerialization.STREAM_CODEC.encode(buf, pkt.component);
            buf.writeMap(pkt.data, ByteBufCodecs.STRING_UTF8, (b, c) -> ComponentSerialization.STREAM_CODEC.encode(buf, c));
            buf.writeCollection(pkt.actions, (b, c) -> ComponentSerialization.STREAM_CODEC.encode(buf, c));
        }
    };

    public static void handle(S2CNpcDialogue pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity instanceof NPCEntity npc)
            ClientHandlers.updateNPCDialogue(npc, pkt.convCtx, pkt.conversationID, pkt.component, pkt.data, pkt.actions);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
