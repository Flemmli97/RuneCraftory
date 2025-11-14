package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.network.S2CEntityPositionPacket;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WeaponHandler<E extends LivingEntity> {

    private static final int COOLDOWN = 6;

    private final E entity;
    private final Supplier<AnimationHandler<E>> animationHandler;

    private AttackAction currentAction = RuneCraftoryAttackActions.NONE.get();
    private String currentAnimation;
    private int comboCount, resetTime, nextCombo = -1;
    private boolean continueAttack;

    private boolean movementUpdate;
    private final Map<DataKey<?>, Object> dataMap = new HashMap<>();

    public WeaponHandler(E entity, Supplier<AnimationHandler<E>> animationHandler) {
        this.entity = entity;
        this.animationHandler = animationHandler;
    }

    public E getEntity() {
        return this.entity;
    }

    public AnimationHandler<E> getAnimationHandler() {
        return this.animationHandler.get();
    }

    public boolean executeAttack(AttackAction action, ItemStack stack) {
        return this.executeAttack(action, stack, null);
    }

    public boolean executeAttack(AttackAction action, ItemStack stack, @Nullable Spell spell) {
        if (this.entity.level().isClientSide)
            return false;
        AttackAction.OverrideType overrideType = this.checkOverride(action, true);
        if (overrideType != AttackAction.OverrideType.NONE) {
            if (overrideType == AttackAction.OverrideType.CONTINUE) {
                this.continueAttack = true;
                return true;
            }
            action.onSetup(this.entity, this);
            this.store(DataKey.USED_WEAPON, stack);
            this.store(DataKey.USED_SPELL, spell);
            this.setState(action, -1);
            return true;
        }
        return false;
    }

    public boolean canExecuteAttack(AttackAction action) {
        return this.canExecuteAttack(action, true);
    }

    public boolean canExecuteAttack(AttackAction action, boolean allowNone) {
        return this.checkOverride(action, allowNone) != AttackAction.OverrideType.NONE;
    }

    private AttackAction.OverrideType checkOverride(AttackAction action, boolean allowNone) {
        if (allowNone && (this.currentAction == RuneCraftoryAttackActions.NONE.get() || this.getAnimationHandler().getAnimation() == null)) {
            return this.getAnimationHandler().getTimeSinceLastChange() < COOLDOWN ? AttackAction.OverrideType.NONE : AttackAction.OverrideType.START;
        }
        if (this.entity.getVehicle() != null && !action.usableOnMounts(this.comboCount + 1)) {
            return AttackAction.OverrideType.NONE;
        }
        if (this.currentAction == action && action.combos() != null) {
            ComboContainer.ComboHandler combo = action.combos().get(this.comboCount - 1);
            return combo != null && combo.canExecute().test(this) ? AttackAction.OverrideType.CONTINUE : AttackAction.OverrideType.NONE;
        }
        return AttackAction.OverrideType.NONE;
    }

    private void setState(AttackAction action, int comboIdx) {
        AttackAction change = this.currentAction.onChange(this.entity, this);
        if (change != null)
            action = change;
        if (comboIdx != -1) {
            this.comboCount = comboIdx;
        }
        if (action == RuneCraftoryAttackActions.NONE.get()) {
            this.resetStates();
        }
        this.currentAction = action;
        this.resetTime = 0;
        this.continueAttack = false;
        AnimationState anim = action.getAnimation(this.entity, this.getComboCount());
        if (this.currentAction != RuneCraftoryAttackActions.NONE.get()) {
            this.comboCount++;
        }
        this.entity.setYBodyRot(this.entity.yHeadRot);
        this.resetHitEntityTracker();
        this.currentAction.onStart(this.entity, this);
        if (anim != null) {
            this.currentAnimation = anim.getID();
        } else {
            this.currentAnimation = null;
        }
        if (!this.entity.level().isClientSide) {
            if (this.entity instanceof Player) {
                LoaderNetwork.INSTANCE.sendToTracking(new S2CWeaponUse(this.currentAction, this.comboCount, this.entity), this.entity);
            }
            if (anim == null) {
                this.getAnimationHandler().setAnimation(null);
            } else {
                this.getAnimationHandler().setAnimation(anim.definition(),
                        anim.getStartTransition(), anim.getEndTransitionTime(),
                        anim.getTick(1), anim.getSpeed());
            }
        }
    }

    public void updateState(AttackAction action, int count) {
        if (!this.entity.level().isClientSide)
            return;
        this.comboCount = count - 1;
        this.setState(action, -1);
    }

    private void resetStates() {
        this.comboCount = 0;
        this.resetTime = 0;
        Set.copyOf(this.dataMap.keySet()).forEach(this::clear);
    }

    public void tick() {
        if (this.nextCombo != -1) {
            this.setState(this.currentAction, this.nextCombo);
            this.nextCombo = -1;
        }
        if (this.currentAction != RuneCraftoryAttackActions.NONE.get()) {
            ItemStack weapon = this.get(DataKey.USED_WEAPON);
            if (!this.isCurrentAnimationDone()) {
                this.currentAction.run(this.entity, weapon, this, this.getAnimationHandler().getAnimation());
                this.applyMoveDirection();
            }
            if (!this.entity.level().isClientSide) {
                ComboContainer.ComboHandler handler = this.currentAction.combos() != null ? this.currentAction.combos().get(this.comboCount - 1) : null;
                if (this.continueAttack && handler != null && handler.canAdvance().test(this)) {
                    if (this.getAnimationHandler().getAnimation() == null) {
                        this.setState(this.currentAction, this.nextCombo);
                    } else {
                        this.nextCombo = handler.advanceTo().get(this);
                    }
                    return;
                }
                boolean reset = this.isCurrentAnimationDone() && (handler == null || ++this.resetTime > handler.resetTime());
                if (!reset) {
                    reset = this.entity.getMainHandItem() != weapon;
                    if (this.entity instanceof ServerPlayer player) {
                        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                        if (reset && weapon.getItem() instanceof ItemSpell) {
                            for (int i = 0; i < data.getInv().getContainerSize(); i++) {
                                if (data.getInv().getItem(i) == weapon) {
                                    reset = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (reset) {
                    this.setState(RuneCraftoryAttackActions.NONE.get(), -1);
                }
            }
        }
    }

    public void postTick() {
        if (this.movementUpdate && !this.getEntity().level().isClientSide) {
            this.movementUpdate = false;
            // Vanilla sends the update at the start of the next tick which is why
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityPositionPacket(this.getEntity()), this.getEntity());
        }
    }

    public boolean shouldContinueAttack() {
        return this.continueAttack;
    }

    public <T> void store(DataKey<T> key, T value) {
        this.dataMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(DataKey<T> key) {
        return (T) this.dataMap.getOrDefault(key, key.defaultValue().get());
    }

    @SuppressWarnings("unchecked")
    public <T> T getSet(DataKey<T> key) {
        return (T) this.dataMap.computeIfAbsent(key, k -> key.defaultValue().get());
    }

    public <T> void clear(DataKey<T> key) {
        this.clearWith(key, key.onClear() == null ? null : val -> key.onClear().accept(this.getEntity(), val));
    }

    @SuppressWarnings("unchecked")
    public <T> void clearWith(DataKey<T> key, @Nullable Consumer<T> apply) {
        if (apply == null) {
            this.dataMap.remove(key);
        } else {
            T current = (T) this.dataMap.remove(key);
            if (current != null) {
                apply.accept(current);
            }
        }
    }

    public AttackAction getCurrentAction() {
        return this.currentAction;
    }

    public void setComboCount(int count) {
        this.comboCount = count;
    }

    public int getComboCount() {
        return this.comboCount;
    }

    public float movementReduction() {
        return this.currentAction.movementReduction(this);
    }

    public boolean isItemSwapBlocked() {
        return this.currentAction.disableItemSwitch();
    }

    public boolean isCurrentAnimationDone() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim == null || (this.currentAnimation != null && !anim.is(this.currentAnimation)) || anim.done(0);
    }

    public boolean matches(Predicate<AnimationState> predicate) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && predicate.test(anim);
    }

    public Set<LivingEntity> getHitEntityTracker() {
        return this.getSet(DataKey.HIT_ENTITIES);
    }

    public void resetHitEntityTracker() {
        this.getHitEntityTracker().clear();
    }

    public void addHitEntityTracker(Collection<LivingEntity> list) {
        this.getHitEntityTracker().addAll(list);
    }

    public boolean isInvulnerable(LivingEntity entity) {
        return this.getCurrentAction().isInvulnerable(entity, this);
    }

    private void applyMoveDirection() {
        Vec3 delta = this.get(DataKey.MOVE_DIRECTION);
        if (delta != null) {
            this.applyDelta(delta);
        }
    }

    public void applyDelta(Vec3 delta) {
        this.getEntity().setDeltaMovement(delta);
        this.movementUpdate = true;
    }

    public void setGravityState(boolean noGravity) {
        this.store(DataKey.GRAVITY, this.getEntity().isNoGravity());
        this.getEntity().setNoGravity(noGravity);
    }
}
