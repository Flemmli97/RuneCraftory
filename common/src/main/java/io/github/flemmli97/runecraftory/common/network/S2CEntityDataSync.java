package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record S2CEntityDataSync(int entityID,
                                DataType dataType,
                                boolean flag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CEntityDataSync> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_entity_data_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntityDataSync> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CEntityDataSync decode(RegistryFriendlyByteBuf buf) {
            return new S2CEntityDataSync(buf.readInt(), buf.readEnum(DataType.class), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CEntityDataSync pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeEnum(pkt.dataType);
            buf.writeBoolean(pkt.flag);
        }
    };

    public static void handle(S2CEntityDataSync pkt, Player player) {
        Entity e = player.level().getEntity(pkt.entityID);
        if (e instanceof LivingEntity living) {
            EntityData data = RunecraftoryAttachments.ENTITY_DATA.get().get(living);
            switch (pkt.dataType) {
                case INVIS -> data.setInvis(pkt.flag ? 1 : 0);
                case ORTHOVIEW -> data.setThirdPersonView(pkt.flag);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum DataType {

        INVIS,
        ORTHOVIEW
    }
}
