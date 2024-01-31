package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.integration.simplequest.ClientSideQuestDisplay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record S2COpenQuestGui(boolean hasQuestBoardQuests,
                              List<ClientSideQuestDisplay> quests) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_open_quest_gui");

    public static S2COpenQuestGui read(FriendlyByteBuf buf) {
        return new S2COpenQuestGui(buf.readBoolean(), buf.readList(ClientSideQuestDisplay::read));
    }

    public static void handle(S2COpenQuestGui pkt) {
        ClientHandlers.openQuestGui(pkt.hasQuestBoardQuests, pkt.quests);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.hasQuestBoardQuests);
        buf.writeCollection(this.quests, (b, t) -> t.write(b));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
