package io.github.flemmli97.runecraftory.common.quests;

import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ClientSideQuestDisplay(ResourceLocation id, Component task, List<? extends Component> description,
                                     @Nullable NPCFeatureContainer features, @Nullable String npcSkin,
                                     boolean active) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientSideQuestDisplay> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ClientSideQuestDisplay decode(RegistryFriendlyByteBuf buf) {
            return new ClientSideQuestDisplay(buf.readResourceLocation(), ComponentSerialization.STREAM_CODEC.decode(buf),
                    buf.readList((b) -> ComponentSerialization.STREAM_CODEC.decode(buf)),
                    buf.readBoolean() ? new NPCFeatureContainer().fromBuffer(buf) : null, buf.readBoolean() ? buf.readUtf() : null, buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientSideQuestDisplay data) {
            buf.writeResourceLocation(data.id);
            ComponentSerialization.STREAM_CODEC.encode(buf, data.task);
            buf.writeCollection(data.description, (b, val) -> ComponentSerialization.STREAM_CODEC.encode(buf, val));
            buf.writeBoolean(data.features != null);
            if (data.features != null)
                data.features.toBuffer(buf);
            buf.writeBoolean(data.npcSkin != null);
            if (data.npcSkin != null)
                buf.writeUtf(data.npcSkin);
            buf.writeBoolean(data.active);
        }
    };
}
