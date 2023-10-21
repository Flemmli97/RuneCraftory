package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestIntegration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2SSubmitQuestBoard implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_quest_board_submit");

    public static C2SSubmitQuestBoard read(FriendlyByteBuf buf) {
        return new C2SSubmitQuestBoard();
    }

    public static void handle(C2SSubmitQuestBoard pkt, ServerPlayer sender) {
        if (sender != null) {
            SimpleQuestIntegration.INST().questBoardComplete(sender);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
