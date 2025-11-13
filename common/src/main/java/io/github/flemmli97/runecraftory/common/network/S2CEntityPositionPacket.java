package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.common.utils.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class S2CEntityPositionPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CEntityPositionPacket> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_entity_position"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntityPositionPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CEntityPositionPacket decode(RegistryFriendlyByteBuf buf) {
            return new S2CEntityPositionPacket(buf.readInt(), StreamCodecs.VEC3.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CEntityPositionPacket pkt) {
            buf.writeInt(pkt.entity);
            StreamCodecs.VEC3.encode(buf, pkt.delta);
        }
    };

    private final int entity;
    private final Vec3 delta;

    public S2CEntityPositionPacket(Entity entity) {
        this(entity.getId(), entity.position());
    }

    private S2CEntityPositionPacket(int entity, Vec3 delta) {
        this.entity = entity;
        this.delta = delta;
    }

    public static void handle(S2CEntityPositionPacket pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity != null) {
            entity.lerpTo(pkt.delta.x(), pkt.delta.y(), pkt.delta.z(), entity.getYRot(), entity.getXRot(), 2);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
