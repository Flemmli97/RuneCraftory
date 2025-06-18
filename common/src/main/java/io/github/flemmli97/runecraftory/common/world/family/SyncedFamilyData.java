package io.github.flemmli97.runecraftory.common.world.family;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record SyncedFamilyData(Optional<Component> father, Optional<Component> mother, Optional<Component> partner,
                               FamilyEntry.Relationship relationship, boolean canProcreate) {

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncedFamilyData> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, SyncedFamilyData>() {
        @Override
        public SyncedFamilyData decode(RegistryFriendlyByteBuf buf) {
            return new SyncedFamilyData(ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).decode(buf),
                    ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).decode(buf),
                    ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).decode(buf),
                    buf.readEnum(FamilyEntry.Relationship.class), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncedFamilyData data) {
            ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).encode(buf, data.father);
            ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).encode(buf, data.mother);
            ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC).encode(buf, data.partner);
            buf.writeEnum(data.relationship);
            buf.writeBoolean(data.canProcreate);
        }
    };
}
