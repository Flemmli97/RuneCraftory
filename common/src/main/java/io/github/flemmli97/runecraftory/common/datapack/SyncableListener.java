package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.common.network.S2CDataPackSync;
import io.github.flemmli97.tenshilib.common.data.SyncableReloadListener;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public interface SyncableListener<T> extends ListenerExtension, SyncableReloadListener {

    StreamCodec<RegistryFriendlyByteBuf, T> codec();

    T toSync();

    void update(HolderLookup.Provider provider, T value);

    @Override
    default void onSync(Collection<ServerPlayer> players) {
        LoaderNetwork.INSTANCE.sendToAll(new S2CDataPackSync<>(this), players);
    }
}
