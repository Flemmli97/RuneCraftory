package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Some components hold registry entries.
 * Due to registration order if these are used during item registration they will not be present and thus they need to hold them as suppliers
 */
public class OptionalSupplier<T> implements Supplier<Optional<T>> {

    public static <T> MapCodec<OptionalSupplier<T>> codec(MapCodec<Optional<T>> codec) {
        return codec.xmap(OptionalSupplier::of, OptionalSupplier::get);
    }

    public static <B extends ByteBuf, T> StreamCodec<B, OptionalSupplier<T>> streamCodec(StreamCodec<B, T> codec) {
        return codec.map(v -> new OptionalSupplier<>(() -> v), s -> s.supplier.get());
    }

    private final Supplier<T> supplier;

    private OptionalSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> OptionalSupplier<T> empty() {
        return new OptionalSupplier<>(() -> null);
    }

    public static <T> OptionalSupplier<T> of(T val) {
        return new OptionalSupplier<>(() -> val);
    }

    public static <T> OptionalSupplier<T> of(Supplier<T> supplier) {
        return new OptionalSupplier<>(supplier);
    }

    public static <T> OptionalSupplier<T> of(Optional<T> opt) {
        T val = opt.orElse(null);
        return new OptionalSupplier<>(() -> val);
    }

    @Override
    public Optional<T> get() {
        return Optional.ofNullable(this.supplier.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof OptionalSupplier<?> other))
            return false;
        return this.get().equals(other.get());
    }

    @Override
    public int hashCode() {
        return this.get().hashCode();
    }
}
