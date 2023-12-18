package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class WeaponHandler {

    private static final float FADE_TICK = 3;

    private AttackAction currentAction = ModAttackActions.NONE.get(), chainTrackerAction = ModAttackActions.NONE.get();
    private int count, timeFrame;

    private AnimatedAction currentAnim, fadingAnim;
    private ItemStack usedWeapon = ItemStack.EMPTY;
    private AttackAction.ActiveActionHandler weaponConsumer;
    private int timeSinceLastChange;

    private int toolCharge;

    private float spinStartRot;
    private final Set<LivingEntity> hitEntityTracker = new HashSet<>();
    private boolean lockLook;

    private static AttackAction.ActiveActionHandler merged(BiConsumer<LivingEntity, AnimatedAction> first, AttackAction.ActiveActionHandler second) {
        if (first == null)
            return second;
        if (second == null)
            return (entity, stack, data, anim) -> first.accept(entity, anim);
        return (entity, stack, data, anim) -> {
            first.accept(entity, anim);
            second.handle(entity, stack, data, anim);
        };
    }

    public static BiConsumer<LivingEntity, AnimatedAction> simpleServersidedAttackExecuter(Runnable run) {
        return (entity, animatedAction) -> {
            if (!entity.level.isClientSide && animatedAction.canAttack()) {
                entity.swing(InteractionHand.MAIN_HAND);
                run.run();
            }
        };
    }

    public boolean doWeaponAttack(LivingEntity entity, AttackAction action, ItemStack stack, @Nullable BiConsumer<LivingEntity, AnimatedAction> attack) {
        if (entity.level.isClientSide || this.canExecuteAction(entity, action) || this.canConsecutiveExecute(entity, action)) {
            if (action.countAdjuster != null)
                this.count = action.countAdjuster.applyAsInt(entity, this);
            this.setAnimationBasedOnState(entity, action, true, attack);
            this.usedWeapon = stack;
            return true;
        }
        return false;
    }

    public boolean canExecuteAction(LivingEntity entity, AttackAction action) {
        return this.currentAction == ModAttackActions.NONE.get();
    }

    public boolean canConsecutiveExecute(LivingEntity entity, AttackAction action) {
        return (this.canExecuteAction(entity, action) || (this.currentAction == action && action.canOverride != null && action.canOverride.test(entity, this)))
                && this.count < action.maxConsecutive.applyAsInt(entity)
                && this.chainTrackerAction == action;
    }

    private void setAnimationBasedOnState(LivingEntity entity, AttackAction action, boolean packet, @Nullable BiConsumer<LivingEntity, AnimatedAction> attack) {
        if (this.currentAction.onEnd != null)
            this.currentAction.onEnd.accept(entity, this);
        if (action == ModAttackActions.NONE.get() && this.count >= this.currentAction.maxConsecutive.applyAsInt(entity)) {
            this.count = 0;
            this.chainTrackerAction = action;
        }
        this.currentAction = action;
        if (action != ModAttackActions.NONE.get()) {
            this.chainTrackerAction = this.currentAction;
        }
        this.weaponConsumer = merged(attack, action.attackExecuter);
        if (action == ModAttackActions.NONE.get())
            this.fadingAnim = this.currentAnim;
        this.currentAnim = action.getAnimation(entity, this.getCurrentCount());
        this.timeSinceLastChange = 0;
        if (this.currentAction != ModAttackActions.NONE.get()) {
            this.count++;
            if (action.timeFrame != null && (this.chainTrackerAction != this.currentAction || this.timeFrame <= 0))
                this.timeFrame = action.timeFrame.applyAsInt(entity);
            if (this.chainTrackerAction == this.currentAction) {
                this.toolCharge = 0;
            }
        }
        if (action == ModAttackActions.NONE.get())
            this.usedWeapon = ItemStack.EMPTY;
        entity.yBodyRot = entity.yHeadRot;
        this.resetHitEntityTracker();
        this.lockLook = false;
        if (this.currentAction.onStart != null)
            this.currentAction.onStart.accept(entity, this);
        if (packet && entity instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CWeaponUse(this.currentAction, this.usedWeapon), serverPlayer);
        }
    }

    public void tick(LivingEntity entity) {
        if (this.currentAnim != null) {
            if (this.currentAnim.tick()) {
                this.setAnimationBasedOnState(entity, ModAttackActions.NONE.get(), false, null);
            } else {
                if (entity instanceof ServerPlayer player) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player).orElse(null);
                    boolean changedItem = entity.getMainHandItem() != this.usedWeapon;
                    if (changedItem && this.usedWeapon.getItem() instanceof ItemSpell && data != null) {
                        for (int i = 0; i < data.getInv().getContainerSize(); i++) {
                            if (data.getInv().getItem(i) == this.usedWeapon) {
                                changedItem = false;
                                break;
                            }
                        }
                    }
                    if (changedItem) {
                        this.setAnimationBasedOnState(entity, ModAttackActions.NONE.get(), true, null);
                    }
                }
                if (this.currentAnim != null && this.weaponConsumer != null)
                    this.weaponConsumer.handle(entity, this.usedWeapon, this, this.currentAnim);
            }
        } else {
            if (--this.timeFrame <= 0) {
                this.count = 0;
                this.chainTrackerAction = ModAttackActions.NONE.get();
                this.toolCharge = 0;
            }
        }
        this.timeSinceLastChange++;
        if (this.timeSinceLastChange >= FADE_TICK)
            this.fadingAnim = null;
    }

    public AttackAction getCurrentAction() {
        return this.currentAction;
    }

    public void updateToolCharge(int charge) {
        this.toolCharge = charge;
    }

    public int getToolCharge() {
        return this.toolCharge;
    }

    public float interpolatedLastChange() {
        if (this.fadingAnim == null)
            return 1;
        return Math.max(0, 1 - this.timeSinceLastChange / FADE_TICK);
    }

    public int getCurrentCount() {
        return this.count;
    }

    public boolean isMovementBlocked() {
        return this.currentAction.disableMovement;
    }

    public boolean isItemSwapBlocked() {
        return this.currentAction.disableItemSwitch;
    }

    public boolean noAnimation() {
        return this.currentAction.disableAnimation;
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

    public AnimatedAction getFadingAnim() {
        return this.fadingAnim;
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

    public void addHitEntityTracker(List<LivingEntity> list) {
        this.hitEntityTracker.addAll(list);
    }

    public boolean isInvulnerable(LivingEntity entity) {
        return this.currentAction.isInvulnerable != null && this.currentAction.isInvulnerable.test(entity, this);
    }
}
