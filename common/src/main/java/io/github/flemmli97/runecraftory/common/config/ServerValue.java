package io.github.flemmli97.runecraftory.common.config;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public abstract class ServerValue<T> {

    private static final Map<String, ServerValue<?>> SYNCABLES = new HashMap<>();

    public static Map<String, ServerValue<?>> getSyncableValues() {
        return ImmutableMap.copyOf(SYNCABLES);
    }

    private T configValue;
    private T useValue;

    public ServerValue(T init, String key) {
        this.configValue = init;
        this.useValue = init;
        if (SYNCABLES.containsKey(key))
            throw new IllegalStateException("Syncable config value with key " + key + " already registered");
        SYNCABLES.put(key, this);
    }

    /**
     * Read from a config
     */
    public void read(T configValue) {
        this.configValue = configValue;
    }

    protected T getConfigValue() {
        return this.configValue;
    }

    public T get() {
        return this.useValue;
    }

    /**
     * Sync the config value from the server
     */
    protected void sync(T value) {
        this.useValue = value;
    }

    public abstract void writeToBuffer(FriendlyByteBuf buf);

    public abstract void readFromBuffer(FriendlyByteBuf buf);

    public static class SyncedBoolean extends ServerValue<Boolean> {

        public SyncedBoolean(Boolean init, String key) {
            super(init, key);
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeBoolean(this.getConfigValue());
        }

        @Override
        public void readFromBuffer(FriendlyByteBuf buf) {
            this.sync(buf.readBoolean());
        }
    }
}
