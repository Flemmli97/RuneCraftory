package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class C2SSubmitQuestBoard implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSubmitQuestBoard> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_quest_board_submit"));

    public static final C2SSubmitQuestBoard INSTANCE = new C2SSubmitQuestBoard();
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSubmitQuestBoard> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private C2SSubmitQuestBoard() {
    }

    public static void handle(C2SSubmitQuestBoard pkt, ServerPlayer sender) {
        QuestHandler.getData(sender).submit(null);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
