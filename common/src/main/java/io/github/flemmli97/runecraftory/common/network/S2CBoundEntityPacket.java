package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.utils.BoundEntityListListener;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class S2CBoundEntityPacket implements CustomPacketPayload {

    public static final Type<S2CBoundEntityPacket> TYPE = new Type<>(RuneCraftory.modRes("s2c_player_entity_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBoundEntityPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CBoundEntityPacket decode(RegistryFriendlyByteBuf buf) {
            return new S2CBoundEntityPacket(buf.readInt(), buf.readInt(), buf.readEnum(Action.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CBoundEntityPacket pkt) {
            buf.writeInt(pkt.entity);
            buf.writeInt(pkt.other);
            buf.writeEnum(pkt.action);
        }
    };

    private final int entity, other;
    private final Action action;

    private S2CBoundEntityPacket(int entity, int other, Action action) {
        this.entity = entity;
        this.other = other;
        this.action = action;
    }

    public static <T extends Entity & BoundEntityListListener> void add(T entity, LivingEntity target) {
        LoaderNetwork.INSTANCE.sendToTracking(new S2CBoundEntityPacket(entity.getId(), target.getId(), Action.ADD), entity);
    }

    public static <T extends Entity & BoundEntityListListener> void remove(T entity, LivingEntity target) {
        LoaderNetwork.INSTANCE.sendToTracking(new S2CBoundEntityPacket(entity.getId(), target.getId(), Action.REMOVE), entity);
    }

    public static <T extends Entity & BoundEntityListListener> void clear(T entity) {
        LoaderNetwork.INSTANCE.sendToTracking(new S2CBoundEntityPacket(entity.getId(), -1, Action.CLEAR), entity);
    }

    public static void handle(S2CBoundEntityPacket pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity instanceof BoundEntityListListener listener) {
            if (pkt.action == Action.CLEAR) {
                listener.getList().clear();
                return;
            }
            Entity target = player.level().getEntity(pkt.other);
            if (target instanceof LivingEntity living) {
                if (pkt.action == Action.ADD) {
                    listener.getList().add(living);
                } else {
                    listener.getList().remove(living);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private enum Action {
        ADD,
        REMOVE,
        CLEAR;
    }
}
