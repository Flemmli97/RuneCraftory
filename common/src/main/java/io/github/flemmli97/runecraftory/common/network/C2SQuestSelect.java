package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SQuestSelect(ResourceLocation quest, boolean active) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_quest_select");

    public static C2SQuestSelect read(FriendlyByteBuf buf) {
        return new C2SQuestSelect(buf.readBoolean() ? buf.readResourceLocation() : null, buf.readBoolean());
    }

    public static void handle(C2SQuestSelect pkt, ServerPlayer sender) {
        if (sender != null) {
            if (pkt.quest() == null)
                SimpleQuestIntegration.INST().resetQuestData(sender);
            else if (pkt.active())
                SimpleQuestIntegration.INST().resetQuest(sender, pkt.quest());
            else
                SimpleQuestIntegration.INST().acceptQuest(sender, pkt.quest());
        }
    }

    public C2SQuestSelect() {
        this(null, false);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.quest != null);
        if (this.quest != null)
            buf.writeResourceLocation(this.quest);
        buf.writeBoolean(this.active);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
