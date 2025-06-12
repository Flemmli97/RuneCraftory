package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.SyncableListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CDataPackSync<T> implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CDataPackSync<?>> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_datapack_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CDataPackSync<?>> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CDataPackSync<?> decode(RegistryFriendlyByteBuf buf) {
            return new S2CDataPackSync<>(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CDataPackSync<?> pkt) {
            buf.writeResourceLocation(pkt.listener.id());
            pkt.toBuffer(buf);
        }
    };

    private final SyncableListener<T> listener;
    private final T data;

    private S2CDataPackSync(RegistryFriendlyByteBuf buf) {
        this.listener = DataPackHandler.INSTANCE.getSyncable(buf.readResourceLocation());
        this.data = this.listener.codec().decode(buf);
    }

    public S2CDataPackSync(SyncableListener<T> listener) {
        this.listener = listener;
        this.data = listener.toSync();
    }

    public static void handle(S2CDataPackSync<?> pkt, Player player) {
        pkt.update(player.registryAccess());
    }

    private void toBuffer(RegistryFriendlyByteBuf buf) {
        this.listener.codec().encode(buf, this.data);
    }

    private void update(HolderLookup.Provider provider) {
        this.listener.update(provider, this.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum SyncedType {
        ITEMSTATS,
        CROPS,
        FOOD,
    }
}
