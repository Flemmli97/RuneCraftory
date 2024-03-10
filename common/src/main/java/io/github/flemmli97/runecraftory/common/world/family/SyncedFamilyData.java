package io.github.flemmli97.runecraftory.common.world.family;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public record SyncedFamilyData(Component father, Component mother, Component partner,
                               FamilyEntry.Relationship relationship, boolean canProcreate) {

    public SyncedFamilyData(FriendlyByteBuf buf) {
        this(buf.readBoolean() ? buf.readComponent() : null,
                buf.readBoolean() ? buf.readComponent() : null,
                buf.readBoolean() ? buf.readComponent() : null,
                buf.readEnum(FamilyEntry.Relationship.class), buf.readBoolean());
    }

    public void toPacket(FriendlyByteBuf buf) {
        buf.writeBoolean(this.father != null);
        if (this.father != null)
            buf.writeComponent(this.father);
        buf.writeBoolean(this.mother != null);
        if (this.mother != null)
            buf.writeComponent(this.mother);
        buf.writeBoolean(this.partner != null);
        if (this.partner != null)
            buf.writeComponent(this.partner);
        buf.writeEnum(this.relationship);
        buf.writeBoolean(this.canProcreate);
    }
}
