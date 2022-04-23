package io.github.flemmli97.runecraftory.platform;

import java.util.function.Supplier;

public class LazyGetter<T> implements Supplier<T> {

    private final Supplier<T> sup;
    private T val;

    public LazyGetter(Supplier<T> sup) {
        this.sup = sup;
    }

    @Override
    public T get() {
        if (this.val == null) {
            this.val = this.sup.get();
        }
        return this.val;
    }
}

