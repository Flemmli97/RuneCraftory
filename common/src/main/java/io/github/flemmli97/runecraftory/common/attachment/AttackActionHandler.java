package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface AttackActionHandler {

    boolean doWeaponAttack(AttackAction action, ItemStack stack, @Nullable Spell spell);

    void tick();

    <T> void store(DataKey<T> key, T value);

    default <T> void clear(DataKey<T> key) {
        this.clearWith(key, key.onClear() == null ? null : val -> key.onClear().accept(this.getEntity(), val));
    }

    <T> void clearWith(DataKey<T> key, @Nullable Consumer<T> apply);

    LivingEntity getEntity();

    float getCurrentTransitionProgress(float partialTicks);

    float getLastTransitionProgress(float partialTicks);

    AnimationState getLastAnimation();

    int getComboCount();

    void setComboCount(int count);

    boolean isScheduledAction();

    default boolean isCurrentAnimationDone() {
        AnimationState anim = this.getAnimation();
        return anim == null || anim.done(0);
    }

    AnimationState getAnimation();

    default void resetHitEntityTracker() {
        this.getHitEntityTracker().clear();
    }

    Set<LivingEntity> getHitEntityTracker();

    default void addHitEntityTracker(Collection<LivingEntity> list) {
        this.getHitEntityTracker().addAll(list);
    }

    default boolean isInvulnerable(LivingEntity entity) {
        return this.getCurrentAction().isInvulnerable(entity, this);
    }

    AttackAction getCurrentAction();

    default void applyMoveDirection() {
        Vec3 move = this.get(DataKey.MOVE_DIRECTION);
        if (move != null)
            this.getEntity().setDeltaMovement(move);
    }

    <T> T get(DataKey<T> key);
}
