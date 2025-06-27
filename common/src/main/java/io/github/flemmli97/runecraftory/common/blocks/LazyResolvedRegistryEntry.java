package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

/**
 * Used for block codecs serialization
 */
public class LazyResolvedRegistryEntry<T> implements Function<HolderLookup.Provider, Holder<T>> {

    public static <T> Codec<LazyResolvedRegistryEntry<T>> codec(ResourceKey<? extends Registry<T>> key) {
        return ResourceKey.codec(key).xmap(LazyResolvedRegistryEntry::new, e -> e.key);
    }

    private final ResourceKey<T> key;
    private Holder<T> entry;

    public LazyResolvedRegistryEntry(ResourceKey<T> key) {
        this.key = key;
    }

    public ResourceKey<T> getKey() {
        return this.key;
    }

    public Holder<T> get(HolderLookup.Provider provider) {
        return this.get(provider.lookupOrThrow(this.key.registryKey()));
    }

    public Holder<T> get(HolderLookup<T> lookup) {
        if (this.entry == null) {
            this.entry = lookup.get(this.key).orElseThrow();
        }
        return this.entry;
    }

    @Override
    public Holder<T> apply(HolderLookup.Provider provider) {
        return this.get(provider);
    }
}
