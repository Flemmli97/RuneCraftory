package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.quests.ClientSideQuestDisplay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record S2COpenQuestGui(boolean hasQuestBoardQuests,
                              List<ClientSideQuestDisplay> quests) implements CustomPacketPayload {

    public static final ResourceLocation ID = RuneCraftory.modRes("s2c_open_quest_gui");
    public static final CustomPacketPayload.Type<S2COpenQuestGui> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_open_quest_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenQuestGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2COpenQuestGui decode(RegistryFriendlyByteBuf buf) {
            return new S2COpenQuestGui(buf.readBoolean(), buf.readList(b -> ClientSideQuestDisplay.STREAM_CODEC.decode(buf)));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2COpenQuestGui pkt) {
            buf.writeBoolean(pkt.hasQuestBoardQuests);
            buf.writeCollection(pkt.quests, (b, t) -> ClientSideQuestDisplay.STREAM_CODEC.encode(buf, t));
        }
    };

    public static void handle(S2COpenQuestGui pkt) {
        ClientHandlers.openQuestGui(pkt.hasQuestBoardQuests, pkt.quests);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
