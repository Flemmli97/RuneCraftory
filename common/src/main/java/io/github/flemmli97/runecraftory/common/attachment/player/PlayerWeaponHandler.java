package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerWeaponHandler implements AttackActionHandler {

    private static final int COOLDOWN = 6;

    private final Player entity;

    private AttackAction currentAction = ModAttackActions.NONE.get();
    private int comboCount;
    private boolean scheduledAction;

    private AnimationState currentAnimation, lastAnimation;

    /**
     * Value used to interpolate animation transitions
     */
    private int timeSinceLastChange;

    private final Set<LivingEntity> hitEntityTracker = new HashSet<>();

    private final Map<DataKey<?>, Object> dataMap = new HashMap<>();

    public PlayerWeaponHandler(Player entity) {
        this.entity = entity;
    }

    public boolean doWeaponAttack(AttackAction action, ItemStack stack) {
        return this.doWeaponAttack(action, stack, null);
    }

    @Override
    public boolean doWeaponAttack(AttackAction action, ItemStack stack, @Nullable Spell spell) {
        AttackAction.OverrideType overrideType = this.checkOverride(action, true);
        if (this.entity.level().isClientSide || overrideType != AttackAction.OverrideType.NONE) {
            if (overrideType == AttackAction.OverrideType.SCHEDULE) {
                this.scheduledAction = true;
                return true;
            }
            action.onSetup(this.entity, this);
            this.setAnimationBasedOnState(action, -1, true);
            this.store(DataKey.USED_WEAPON, stack);
            this.store(DataKey.USED_SPELL, spell);
            return true;
        }
        return false;
    }

    public boolean canExecuteAction(AttackAction action) {
        return this.canExecuteAction(action, true);
    }

    public boolean canExecuteAction(AttackAction action, boolean allowNone) {
        return this.checkOverride(action, allowNone) != AttackAction.OverrideType.NONE;
    }

    private AttackAction.OverrideType checkOverride(AttackAction action, boolean allowNone) {
        if (allowNone && (this.currentAction == ModAttackActions.NONE.get() || this.currentAnimation == null)) {
            return this.timeSinceLastChange < COOLDOWN ? AttackAction.OverrideType.NONE : AttackAction.OverrideType.REPLACE;
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

    private void setAnimationBasedOnState(AttackAction action, int comboIdx, boolean packet) {
        AttackAction change = this.currentAction.onChange(this.entity, this);
        if (change != null)
            action = change;
        if (comboIdx != -1)
            this.comboCount = comboIdx;
        if (action == ModAttackActions.NONE.get()) {
            this.resetStates();
        }
        this.lastAnimation = this.currentAnimation;
        this.timeSinceLastChange = 0;
        this.currentAction = action;
        this.scheduledAction = false;
        this.currentAnimation = action.getAnimation(this.entity, this.getComboCount());
        if (this.currentAction != ModAttackActions.NONE.get()) {
            this.comboCount++;
        }
        this.entity.yBodyRot = this.entity.yHeadRot;
        this.resetHitEntityTracker();
        this.currentAction.onStart(this.entity, this);
        if (!this.entity.level().isClientSide && packet) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CWeaponUse(this.currentAction, this.get(DataKey.USED_WEAPON),
                    this.comboCount - 1, this.entity), this.entity);
        }
    }

    public void clientSideUpdate(AttackAction action, ItemStack stack, int count) {
        if (!this.entity.level().isClientSide)
            return;
        this.comboCount = count;
        this.setAnimationBasedOnState(action, -1, false);
        this.store(DataKey.USED_WEAPON, stack);
    }

    private void resetStates() {
        this.comboCount = 0;
        this.hitEntityTracker.clear();
        Set.copyOf(this.dataMap.keySet()).forEach(this::clear);
    }

    @Override
    public void tick() {
        if (this.currentAnimation != null) {
            ComboContainer.ComboHandler handler = this.currentAction.combos() != null ? this.currentAction.combos().get(this.comboCount - 1) : null;
            if (this.scheduledAction && handler != null && handler.canAdvance().test(this)) {
                this.setAnimationBasedOnState(this.currentAction, handler.advanceTo().get(this), true);
                return;
            } else if (this.currentAnimation.tick(1 + (int) (this.currentAnimation.getSpeed() * (handler != null ? handler.resetTime() : 0)))) {
                this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1, false);
            } else {
                ItemStack weapon = this.get(DataKey.USED_WEAPON);
                if (this.entity instanceof ServerPlayer player) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player);
                    boolean changedItem = this.entity.getMainHandItem() != weapon;
                    if (changedItem && weapon.getItem() instanceof ItemSpell) {
                        for (int i = 0; i < data.getInv().getContainerSize(); i++) {
                            if (data.getInv().getItem(i) == weapon) {
                                changedItem = false;
                                break;
                            }
                        }
                    }
                    if (changedItem) {
                        this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1, true);
                    }
                }
                this.currentAction.run(this.entity, weapon, this, this.currentAnimation);
            }
        }
        this.timeSinceLastChange++;
        if (this.lastAnimation != null && this.timeSinceLastChange > this.lastAnimation.getEndTransitionTime()) {
            this.lastAnimation = null;
        }
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isScheduledAction() {
        return this.scheduledAction;
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
        if (this.currentAnimation == null) {
            return 1;
        }
        return (float) this.currentAnimation.getStartTransitionProgress(partialTicks);
    }

    @Override
    public float getLastTransitionProgress(float partialTicks) {
        if (this.lastAnimation == null) {
            return 0;
        }
        return 1 - Mth.clamp((this.timeSinceLastChange - 1 + partialTicks) / this.lastAnimation.getEndTransitionTime(), 0, 1);
    }

    @Override
    public AnimationState getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public AnimationState getLastAnimation() {
        return this.lastAnimation;
    }

    @Override
    public void setComboCount(int count) {
        this.comboCount = count;
    }

    @Override
    public int getComboCount() {
        return this.comboCount;
    }

    public float movementReduction() {
        return this.currentAction.movementReduction(this.currentAnimation);
    }

    public boolean isItemSwapBlocked() {
        return this.currentAction.disableItemSwitch();
    }

    @Override
    public Set<LivingEntity> getHitEntityTracker() {
        return this.hitEntityTracker;
    }
}
