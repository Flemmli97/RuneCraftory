package io.github.flemmli97.runecraftory.common.integration.simplequest;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ClientSideQuestDisplay(ResourceLocation id, Component task, List<? extends Component> description,
                                     boolean active) {

    public static ClientSideQuestDisplay read(FriendlyByteBuf buf) {
        return new ClientSideQuestDisplay(buf.readResourceLocation(), buf.readComponent(), buf.readList(FriendlyByteBuf::readComponent), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.id);
        buf.writeComponent(this.task);
        buf.writeCollection(this.description, FriendlyByteBuf::writeComponent);
        buf.writeBoolean(this.active);
    }
}
