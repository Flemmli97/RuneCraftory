package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class EntityWeaponHandler<T extends LivingEntity & IAnimated> implements AttackActionHandler {

    private final T entity;

    private AttackAction currentAction = ModAttackActions.NONE.get();
    private int comboCount;

    private final Set<LivingEntity> hitEntityTracker = new HashSet<>();
    private boolean scheduledAction;

    private final Map<DataKey<?>, Object> dataMap = new HashMap<>();

    public EntityWeaponHandler(T entity) {
        this.entity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public boolean doWeaponAttack(AttackAction action, ItemStack stack, @Nullable Spell spell) {
        AttackAction.OverrideType overrideType = this.checkOverride(action, true);
        if (!this.entity.level().isClientSide && overrideType != AttackAction.OverrideType.NONE) {
            if (overrideType == AttackAction.OverrideType.SCHEDULE) {
                this.scheduledAction = true;
                return true;
            }
            action.onSetup(this.entity, this);
            this.setAnimationBasedOnState(action, -1);
            this.store(DataKey.USED_WEAPON, stack);
            this.store(DataKey.USED_SPELL, spell);
            return true;
        }
        return false;
    }

    private AttackAction.OverrideType checkOverride(AttackAction action, boolean allowNone) {
        if (allowNone && (this.currentAction == ModAttackActions.NONE.get() || this.getAnimation() == null)) {
            return AttackAction.OverrideType.REPLACE;
        }
        if (this.entity.getVehicle() != null && !action.usableOnMounts(this.comboCount + 1)) {
            return AttackAction.OverrideType.NONE;
        }
        if (this.currentAction == action && action.combos() != null) {
            ComboContainer.ComboHandler combo = action.combos().get(this.comboCount - 1);
            return combo != null && combo.canExecute().test(this) ? AttackAction.OverrideType.SCHEDULE : AttackAction.OverrideType.NONE;
        }
        return AttackAction.OverrideType.NONE;
    }

    private void setAnimationBasedOnState(AttackAction action, int comboIdx) {
        AttackAction change = this.currentAction.onChange(this.entity, this);
        if (change != null)
            action = change;
        if (comboIdx != -1)
            this.comboCount = comboIdx;
        if (action == ModAttackActions.NONE.get()) {
            this.resetStates();
        }
        this.currentAction = action;
        this.scheduledAction = false;
        AnimatedAction anim = action.getAnimation(this.entity, this.getComboCount());
        if (this.currentAction != ModAttackActions.NONE.get()) {
            this.comboCount++;
        }
        this.entity.yBodyRot = this.entity.yHeadRot;
        this.resetHitEntityTracker();
        this.currentAction.onStart(this.entity, this);
        if (!this.entity.level().isClientSide) {
            if (anim == null) {
                this.entity.getAnimationHandler().setAnimation(null);
            } else {
                this.entity.getAnimationHandler().setAnimation(anim,
                        anim.getStartTransition(), anim.getEndTransitionTime(),
                        anim.getTick(1));
            }
        }
    }

    private void resetStates() {
        this.comboCount = 0;
        this.hitEntityTracker.clear();
        Set.copyOf(this.dataMap.keySet()).forEach(this::clear);
    }

    @Override
    public void tick() {
        if (this.currentAction != ModAttackActions.NONE.get()) {
            ComboContainer.ComboHandler handler = this.currentAction.combos() != null ? this.currentAction.combos().get(this.comboCount - 1) : null;
            ItemStack weapon = this.get(DataKey.USED_WEAPON);
            boolean changedItem = this.entity.getMainHandItem() != weapon;
            if (changedItem) {
                this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1);
                return;
            }
            if (this.getAnimation() == null) {
                this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1);
            } else {
                this.currentAction.run(this.entity, weapon, this, this.getAnimation());
            }
            if (this.scheduledAction && handler != null && handler.canAdvance().test(this)) {
                this.setAnimationBasedOnState(this.currentAction, handler.advanceTo().get(this));
            }
        }
    }

    @Override
    public <T> void store(DataKey<T> key, T value) {
        this.dataMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(DataKey<T> key) {
        return (T) this.dataMap.getOrDefault(key, key.defaultValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void clearWith(DataKey<T> key, @Nullable Consumer<T> apply) {
        if (apply == null) {
            this.dataMap.remove(key);
        } else {
            apply.accept((T) this.dataMap.remove(key));
        }
    }

    @Override
    public AttackAction getCurrentAction() {
        return this.currentAction;
    }

    @Override
    public float getCurrentTransitionProgress(float partialTicks) {
        return this.entity.getAnimationHandler().getCurrentTransitionProgress(partialTicks);
    }

    @Override
    public float getLastTransitionProgress(float partialTicks) {
        return this.entity.getAnimationHandler().getLastTransitionProgress(partialTicks);
    }

    @Override
    public AnimatedAction getAnimation() {
        return this.entity.getAnimationHandler().getAnimation();
    }

    @Override
    public AnimatedAction getLastAnimation() {
        return this.entity.getAnimationHandler().getLastAnimation();
    }

    @Override
    public void setComboCount(int count) {
        this.comboCount = count;
    }

    @Override
    public int getComboCount() {
        return this.comboCount;
    }

    @Override
    public boolean isScheduledAction() {
        return this.scheduledAction;
    }

    @Override
    public Set<LivingEntity> getHitEntityTracker() {
        return this.hitEntityTracker;
    }
}
