package io.github.flemmli97.runecraftory.common.entities.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SyncableEntityData {

    private static final Map<ResourceLocation, SyncedEntityData<?>> REGISTRY = new HashMap<>();

    public static synchronized <T> SyncedEntityData<T> register(ResourceLocation id, EntityDataSerializer<T> serializer) {
        return register(new SyncedEntityData<>(id, serializer));
    }

    public static synchronized <T> SyncedEntityData<T> register(SyncedEntityData<T> inst) {
        if (REGISTRY.putIfAbsent(inst.id, inst) != null)
            throw new IllegalStateException("ID is already registered");
        return inst;
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> SyncedEntityData<T> get(ResourceLocation id) {
        return (SyncedEntityData<T>) REGISTRY.get(id);
    }

    public record SyncedEntityData<T>(ResourceLocation id, EntityDataSerializer<T> serializer) {
    }

    public static class SyncedContainer<T> {

        private final SyncedEntityData<T> syncedEntityData;
        private final T value;

        public SyncedContainer(SyncedEntityData<T> syncedEntityData, T value) {
            this.syncedEntityData = syncedEntityData;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public <E> void runIf(SyncedEntityData<E> other, Consumer<E> cons) {
            if (this.syncedEntityData.equals(other)) {
                E v = (E) this.value;
                cons.accept(v);
            }
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeResourceLocation(this.syncedEntityData.id);
            buf.writeBoolean(this.value != null);
            if (this.value != null)
                this.syncedEntityData.serializer().write(buf, this.value);
        }

        public static <T> SyncedContainer<T> from(FriendlyByteBuf buf) {
            ResourceLocation id = buf.readResourceLocation();
            SyncedEntityData<T> data = get(id);
            boolean none = buf.readBoolean();
            return new SyncedContainer<>(data, none ? data.serializer().read(buf) : null);
        }
    }
}
