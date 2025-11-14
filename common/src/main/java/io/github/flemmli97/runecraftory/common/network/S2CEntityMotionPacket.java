package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.common.utils.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class S2CEntityMotionPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CEntityMotionPacket> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_entity_position"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntityMotionPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CEntityMotionPacket decode(RegistryFriendlyByteBuf buf) {
            return new S2CEntityMotionPacket(buf.readInt(), StreamCodecs.VEC3.decode(buf), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CEntityMotionPacket pkt) {
            buf.writeInt(pkt.entity);
            StreamCodecs.VEC3.encode(buf, pkt.vec3);
            buf.writeBoolean(pkt.delta);
        }
    };

    private final int entity;
    private final Vec3 vec3;
    private final boolean delta;

    public S2CEntityMotionPacket(Entity entity, boolean delta) {
        this(entity.getId(), delta ? entity.getDeltaMovement() : entity.position(), delta);
    }

    private S2CEntityMotionPacket(int entity, Vec3 vec3, boolean delta) {
        this.entity = entity;
        this.vec3 = vec3;
        this.delta = delta;
    }

    public static void handle(S2CEntityMotionPacket pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity != null) {
            if (pkt.delta) {
                entity.setDeltaMovement(pkt.vec3);
            } else {
                entity.lerpTo(pkt.vec3.x(), pkt.vec3.y(), pkt.vec3.z(), entity.getYRot(), entity.getXRot(), 2);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
