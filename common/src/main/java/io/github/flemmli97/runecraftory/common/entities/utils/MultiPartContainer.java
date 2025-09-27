package io.github.flemmli97.runecraftory.common.entities.utils;

import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;

import java.util.function.Supplier;

/**
 * Simple multipart entity handler
 */
public class MultiPartContainer {

    private final Supplier<MultiPartEntity> sup;
    private MultiPartEntity entity;

    public MultiPartContainer(Supplier<MultiPartEntity> sup) {
        this.sup = sup;
    }

    public void removeEntity() {
        if (this.entity != null) {
            this.entity.discard();
            this.entity = null;
        }
    }

    public void tick() {
        if (this.entity == null) {
            this.entity = this.sup.get();
        }
        this.entity.parentTick();
    }
}
