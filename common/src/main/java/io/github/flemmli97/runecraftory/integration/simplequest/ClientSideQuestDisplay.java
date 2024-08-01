package io.github.flemmli97.runecraftory.integration.simplequest;

import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ClientSideQuestDisplay(ResourceLocation id, Component task, List<? extends Component> description,
                                     @Nullable NPCFeatureContainer features, @Nullable String npcSkin,
                                     boolean active) {

    public static ClientSideQuestDisplay read(FriendlyByteBuf buf) {
        return new ClientSideQuestDisplay(buf.readResourceLocation(), buf.readComponent(), buf.readList(FriendlyByteBuf::readComponent),
                buf.readBoolean() ? new NPCFeatureContainer().fromBuffer(buf) : null, buf.readBoolean() ? buf.readUtf() : null, buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.id);
        buf.writeComponent(this.task);
        buf.writeCollection(this.description, FriendlyByteBuf::writeComponent);
        buf.writeBoolean(this.features != null);
        if (this.features != null)
            this.features.toBuffer(buf);
        buf.writeBoolean(this.npcSkin != null);
        if (this.npcSkin != null)
            buf.writeUtf(this.npcSkin);
        buf.writeBoolean(this.active);
    }
}
