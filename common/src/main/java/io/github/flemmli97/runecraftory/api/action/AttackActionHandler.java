package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface AttackActionHandler {

    LivingEntity getEntity();

    boolean doWeaponAttack(AttackAction action, ItemStack stack, @Nullable Spell spell);

    void tick();

    <T> void store(DataKey<T> key, T value);

    <T> T get(DataKey<T> key);

    default <T> void clear(DataKey<T> key) {
        this.clearWith(key, key.onClear() == null ? null : val -> key.onClear().accept(this.getEntity(), val));
    }

    <T> void clearWith(DataKey<T> key, @Nullable Consumer<T> apply);

    AttackAction getCurrentAction();

    float getCurrentTransitionProgress(float partialTicks);

    float getLastTransitionProgress(float partialTicks);

    AnimationState getAnimation();

    AnimationState getLastAnimation();

    void setComboCount(int count);

    int getComboCount();

    boolean isScheduledAction();

    Set<LivingEntity> getHitEntityTracker();

    default boolean isCurrentAnimationDone() {
        AnimationState anim = this.getAnimation();
        return anim == null || anim.done(0);
    }

    default void resetHitEntityTracker() {
        this.getHitEntityTracker().clear();
    }

    default void addHitEntityTracker(Collection<LivingEntity> list) {
        this.getHitEntityTracker().addAll(list);
    }

    default boolean isInvulnerable(LivingEntity entity) {
        return this.getCurrentAction().isInvulnerable(entity, this);
    }

    default void applyMoveDirection() {
        Vec3 move = this.get(DataKey.MOVE_DIRECTION);
        if (move != null)
            this.getEntity().setDeltaMovement(move);
    }
}
