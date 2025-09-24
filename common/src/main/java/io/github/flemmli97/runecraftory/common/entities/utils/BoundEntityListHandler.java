package io.github.flemmli97.runecraftory.common.entities.utils;

import io.github.flemmli97.runecraftory.common.network.S2CBoundEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Syncs a list of entities to the client.
 * By default, handles players as player movement is client authorative and having e.g. movement handling client side makes it smoother
 */
public class BoundEntityListHandler<T extends Entity & BoundEntityListListener> implements Iterable<LivingEntity> {

    private final T entity;
    private final Predicate<LivingEntity> syncTest;
    private final List<LivingEntity> entities = new ArrayList<>();

    public BoundEntityListHandler(T entity) {
        this(entity, living -> living instanceof Player);
    }

    public BoundEntityListHandler(T entity, Predicate<LivingEntity> syncTest) {
        this.entity = entity;
        this.syncTest = syncTest;
    }

    public void add(LivingEntity entity) {
        this.entities.add(entity);
        if (!this.entity.level().isClientSide && this.syncTest.test(entity)) {
            S2CBoundEntityPacket.add(this.entity, entity);
        }
    }

    public void remove(LivingEntity entity) {
        if (this.entities.remove(entity) && !this.entity.level().isClientSide && this.syncTest.test(entity)) {
            S2CBoundEntityPacket.remove(this.entity, entity);
        }
    }

    public void clear() {
        this.entities.clear();
        if (!this.entity.level().isClientSide) {
            S2CBoundEntityPacket.clear(this.entity);
        }
    }

    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    public boolean has(LivingEntity entity) {
        return this.entities.contains(entity);
    }

    @Override
    public Iterator<LivingEntity> iterator() {
        return this.entities.iterator();
    }
}
