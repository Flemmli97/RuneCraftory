package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record C2SProcreationRequest(int id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SProcreationRequest> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_procreation_request"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SProcreationRequest> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SProcreationRequest decode(RegistryFriendlyByteBuf buf) {
            return new C2SProcreationRequest(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SProcreationRequest pkt) {
            buf.writeInt(pkt.id);
        }
    };

    public C2SProcreationRequest(Entity entity) {
        this(entity.getId());
    }

    public static void handle(C2SProcreationRequest pkt, ServerPlayer sender) {
        Entity entity = sender.level().getEntity(pkt.id);
        if (entity instanceof EntityNPCBase npc) {
            npc.procreateWith(sender);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
