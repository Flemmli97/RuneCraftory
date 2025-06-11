package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.data.MobUpdateHandler;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CMobUpdate implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CMobUpdate> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_mob_update_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMobUpdate> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CMobUpdate decode(RegistryFriendlyByteBuf buf) {
            return new S2CMobUpdate(buf.readInt(), SyncableEntityData.SyncedContainer.from(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CMobUpdate pkt) {
            buf.writeInt(pkt.entity);
            pkt.value.write(buf);
        }
    };

    private final int entity;
    private final SyncableEntityData.SyncedContainer<?> value;

    private S2CMobUpdate(int entity, SyncableEntityData.SyncedContainer<?> value) {
        this.entity = entity;
        this.value = value;
    }

    public static <T extends Entity & MobUpdateHandler, D> void send(T entity, SyncableEntityData.SyncedEntityData<D> key, D value) {
        if (!entity.level().isClientSide)
            LoaderNetwork.INSTANCE.sendToTracking(new S2CMobUpdate(entity.getId(), new SyncableEntityData.SyncedContainer<>(key, value)), entity);
    }

    public static void handle(S2CMobUpdate pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entity);
        if (entity instanceof MobUpdateHandler handler) {
            handler.onUpdate(pkt.value);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
