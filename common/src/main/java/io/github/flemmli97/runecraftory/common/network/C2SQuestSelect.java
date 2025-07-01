package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.quests.QuestData;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SQuestSelect(ResourceLocation quest, boolean active) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SQuestSelect> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_quest_select"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SQuestSelect> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SQuestSelect decode(RegistryFriendlyByteBuf buf) {
            return new C2SQuestSelect(buf.readBoolean() ? buf.readResourceLocation() : null, buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SQuestSelect pkt) {
            buf.writeBoolean(pkt.quest != null);
            if (pkt.quest != null)
                buf.writeResourceLocation(pkt.quest);
            buf.writeBoolean(pkt.active);
        }
    };

    public C2SQuestSelect() {
        this(null, false);
    }

    public static C2SQuestSelect read(RegistryFriendlyByteBuf buf) {
        return new C2SQuestSelect(buf.readBoolean() ? buf.readResourceLocation() : null, buf.readBoolean());
    }

    public static void handle(C2SQuestSelect pkt, ServerPlayer sender) {
        QuestData data = QuestHandler.getData(sender);
        if (pkt.quest() == null)
            data.setQuestboardQuests(null);
        else if (pkt.active())
            data.reset(pkt.quest());
        else
            data.acceptQuest(pkt.quest());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
