package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WeaponHandler {

    private static final float FADE_TICK = 5;
    private static final int COOLDOWN = 6;

    private final LivingEntity entity;

    private AttackAction currentAction = ModAttackActions.NONE.get();
    private int comboCount;
    private boolean scheduledAction;

    private AnimatedAction currentAnim, lastAnim;
    private ItemStack usedWeapon = ItemStack.EMPTY;
    private Spell spell;
    /**
     * Whether {@link Spell#use} should be called at {@link AttackAction#onStart}
     * Usually holds true for weapon abilities
     */
    private boolean consumeSpellOnStart;
    /**
     * Value used to interpolate animation transitions
     */
    private int timeSinceLastChange;

    private ToolUseData toolUseData;

    private float spinStartRot;
    private final Set<LivingEntity> hitEntityTracker = new HashSet<>();
    private boolean lockLook;

    private Vec3 moveDir;
    private boolean oldGravity;
    private int moveDuration;
    private Entity target;

    public WeaponHandler(LivingEntity entity) {
        this.entity = entity;
    }

    public boolean doWeaponAttack(AttackAction action, ItemStack stack) {
        return this.doWeaponAttack(action, stack, null);
    }

    public boolean doWeaponAttack(AttackAction action, ItemStack stack, @Nullable Spell spell) {
        AttackAction.OverrideType overrideType = this.checkOverride(action, true);
        if (this.entity.level.isClientSide || overrideType != AttackAction.OverrideType.NONE) {
            if (overrideType == AttackAction.OverrideType.SCHEDULE) {
                this.scheduledAction = true;
                return true;
            }
            action.onSetup(this.entity, this);
            this.spell = spell;
            this.usedWeapon = stack;
            this.setAnimationBasedOnState(action, -1, true);
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
        if (allowNone && (this.currentAction == ModAttackActions.NONE.get() || this.currentAnim == null)) {
            return this.timeSinceLastChange < COOLDOWN ? AttackAction.OverrideType.NONE : AttackAction.OverrideType.REPLACE;
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
        this.moveDir = null;
        if (comboIdx != -1)
            this.comboCount = comboIdx;
        if (action == ModAttackActions.NONE.get()) {
            this.resetStates();
        }
        this.lastAnim = this.currentAnim;
        this.timeSinceLastChange = 0;
        this.currentAction = action;
        this.scheduledAction = false;
        this.currentAnim = action.getAnimation(this.entity, this.getComboCount());
        if (this.currentAction != ModAttackActions.NONE.get()) {
            this.comboCount++;
        } else
            this.usedWeapon = ItemStack.EMPTY;
        this.entity.yBodyRot = this.entity.yHeadRot;
        this.resetHitEntityTracker();
        this.lockLook = false;
        this.currentAction.onStart(this.entity, this);
        this.consumeSpellOnStart = false;
        if (!this.entity.level.isClientSide) {
            if (this.entity instanceof IAnimated animated && this.currentAnim != null) {
                animated.getAnimationHandler().setAnimation(this.currentAnim);
            }
            if (packet) {
                Platform.INSTANCE.sendToTrackingAndSelf(new S2CWeaponUse(this.currentAction, this.usedWeapon, this.comboCount - 1, this.entity), this.entity);
            }
        } else if (this.entity instanceof IAnimated animated) {
            // Tick once on client. Otherwise it can flicker for some reason. dont wanna investigate atm
            animated.getAnimationHandler().tick();
        }
    }

    public void clientSideUpdate(AttackAction action, ItemStack stack, int count) {
        if (!this.entity.level.isClientSide)
            return;
        this.comboCount = count;
        this.setAnimationBasedOnState(action, -1, false);
        this.usedWeapon = stack;
    }

    private void resetStates() {
        this.spell = null;
        this.comboCount = 0;
        this.toolUseData = null;
        this.hitEntityTracker.clear();
        this.target = null;
    }

    public void tick() {
        if (this.currentAnim != null) {
            ComboContainer.ComboHandler handler = this.currentAction.combos() != null ? this.currentAction.combos().get(this.comboCount - 1) : null;
            if (this.scheduledAction && handler != null && handler.canAdvance().test(this)) {
                this.setAnimationBasedOnState(this.currentAction, handler.advanceTo().get(this), true);
                return;
            } else if (this.currentAnim.tick(1 + (int) (this.currentAnim.getSpeed() * (handler != null ? handler.resetTime() : 0)))) {
                this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1, false);
            } else {
                if (this.entity instanceof ServerPlayer player) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player).orElse(null);
                    boolean changedItem = this.entity.getMainHandItem() != this.usedWeapon;
                    if (changedItem && this.usedWeapon.getItem() instanceof ItemSpell && data != null) {
                        for (int i = 0; i < data.getInv().getContainerSize(); i++) {
                            if (data.getInv().getItem(i) == this.usedWeapon) {
                                changedItem = false;
                                break;
                            }
                        }
                    }
                    if (changedItem) {
                        this.setAnimationBasedOnState(ModAttackActions.NONE.get(), -1, true);
                    }
                }
                this.currentAction.run(this.entity, this.usedWeapon, this, this.currentAnim);
            }
        }
        if (this.moveDir != null) {
            this.entity.setDeltaMovement(this.moveDir);
            this.moveDuration--;
            if (this.moveDuration <= 0)
                this.moveDir = null;
        }
        this.timeSinceLastChange++;
        if (this.interpolatedLastChange(1) == 1)
            this.lastAnim = null;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public boolean isScheduledAction() {
        return this.scheduledAction;
    }

    public boolean isCurrentAnimationDone() {
        return this.currentAnim != null && this.currentAnim.isPastTick(this.currentAnim.getLength());
    }

    public AttackAction getCurrentAction() {
        return this.currentAction;
    }

    public void updateToolCharge(ToolUseData toolUseData) {
        this.toolUseData = toolUseData;
    }

    public ToolUseData getToolUseData() {
        return this.toolUseData;
    }

    public float interpolatedLastChange(float partialTicks) {
        return Mth.clamp((this.timeSinceLastChange + partialTicks) / FADE_TICK, 0, 1);
    }

    public ItemStack getUsedWeapon() {
        return this.usedWeapon;
    }

    public void setComboCount(int count) {
        this.comboCount = count;
    }

    public int getComboCount() {
        return this.comboCount;
    }

    public float movementReduction() {
        return this.currentAction.movementReduction(this.currentAnim);
    }

    public boolean isItemSwapBlocked() {
        return this.currentAction.disableItemSwitch();
    }

    public boolean lockedLook() {
        return this.lockLook;
    }

    public void lockLook(boolean flag) {
        this.lockLook = flag;
    }

    public AnimatedAction getCurrentAnim() {
        return this.currentAnim;
    }

    public AnimatedAction getLastAnim() {
        return this.lastAnim;
    }

    public Spell getSpellToCast() {
        return this.spell;
    }

    public void setSpinStartRot(float rot) {
        this.spinStartRot = rot;
    }

    public float getSpinStartRot() {
        return this.spinStartRot;
    }

    public Set<LivingEntity> getHitEntityTracker() {
        return this.hitEntityTracker;
    }

    public void resetHitEntityTracker() {
        this.hitEntityTracker.clear();
    }

    public void addHitEntityTracker(Collection<LivingEntity> list) {
        this.hitEntityTracker.addAll(list);
    }

    public boolean isInvulnerable(LivingEntity entity) {
        return this.currentAction.isInvulnerable(entity, this);
    }

    public void setMoveTargetDir(Vec3 direction, AnimatedAction animation, double endTick) {
        this.setMoveTargetDir(direction, animation, Mth.ceil(endTick * 20));
    }

    public void setMoveTargetDir(Vec3 direction, AnimatedAction animation, int endTick) {
        double duration = Math.max(1, (endTick - animation.getTick()) / animation.getSpeed());
        this.moveDir = direction.scale(1d / duration);
        this.moveDuration = Mth.ceil(duration);
    }

    public Entity getTarget() {
        return this.target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public void clearMoveTarget() {
        this.moveDir = null;
    }

    public void setNoGravity(LivingEntity entity) {
        this.oldGravity = entity.isNoGravity();
        entity.setNoGravity(true);
    }

    public boolean consumeSpellOnStart() {
        return this.consumeSpellOnStart;
    }

    public void setConsumeSpellOnStart() {
        this.consumeSpellOnStart = true;
    }

    public void restoreGravity(LivingEntity entity) {
        entity.setNoGravity(this.oldGravity);
    }

    public record ToolUseData(HitResult result, int charge) {
    }
}
