package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class WeaponHandler {

    private static final float FADE_TICK = 3;

    private AttackAction currentAction = AttackAction.NONE, chainTrackerAction = AttackAction.NONE;
    private int count, timeFrame;

    private AnimatedAction currentAnim, fadingAnim;
    private ItemStack usedWeapon = ItemStack.EMPTY;
    private AttackAction.ActiveActionHandler weaponConsumer;
    private int timeSinceLastChange;

    private int toolCharge;

    private static AttackAction.ActiveActionHandler merged(BiConsumer<Player, AnimatedAction> first, AttackAction.ActiveActionHandler second) {
        if (first == null)
            return second;
        if (second == null)
            return (player, stack, data, anim) -> first.accept(player, anim);
        return (player, stack, data, anim) -> {
            first.accept(player, anim);
            second.handle(player, stack, data, anim);
        };
    }

    public static BiConsumer<Player, AnimatedAction> simpleServersidedAttackExecuter(Runnable run) {
        return (player, animatedAction) -> {
            if (!player.level.isClientSide && animatedAction.canAttack()) {
                player.swing(InteractionHand.MAIN_HAND);
                run.run();
            }
        };
    }

    public boolean doWeaponAttack(Player player, AttackAction action, ItemStack stack, @Nullable BiConsumer<Player, AnimatedAction> attack) {
        if (this.canExecuteAction(player, action) || this.canConsecutiveExecute(player, action)) {
            this.setAnimationBasedOnState(player, action, true, attack);
            this.usedWeapon = stack;
            return true;
        }
        return false;
    }

    public boolean canExecuteAction(Player player, AttackAction action) {
        return this.currentAction == AttackAction.NONE;
    }

    public boolean canConsecutiveExecute(Player player, AttackAction action) {
        return (this.canExecuteAction(player, action) || (this.currentAction == action && action.canOverride != null && action.canOverride.apply(player, this)))
                && this.count < action.maxConsecutive.apply(player)
                && this.chainTrackerAction == action;
    }

    private void setAnimationBasedOnState(Player player, AttackAction action, boolean packet, @Nullable BiConsumer<Player, AnimatedAction> attack) {
        if (this.currentAction.onEnd != null)
            this.currentAction.onEnd.accept(player);
        if (action == AttackAction.NONE && this.count >= this.currentAction.maxConsecutive.apply(player)) {
            this.count = 0;
            if (this.chainTrackerAction.nextAction != null)
                action = this.chainTrackerAction.nextAction.apply(player, this);
            this.chainTrackerAction = action;
        }
        this.currentAction = action;
        if (action != AttackAction.NONE) {
            this.chainTrackerAction = this.currentAction;
        }
        this.weaponConsumer = merged(attack, action.attackExecuter);
        if (action == AttackAction.NONE)
            this.fadingAnim = this.currentAnim;
        this.currentAnim = action.anim.apply(player, this);
        this.timeSinceLastChange = 0;
        if (this.currentAction != AttackAction.NONE) {
            this.count++;
            if (action.timeFrame != null && (this.chainTrackerAction != this.currentAction || this.timeFrame <= 0))
                this.timeFrame = action.timeFrame.apply(player);
            if (this.chainTrackerAction == this.currentAction) {
                this.toolCharge = 0;
            }
        }
        if (action == AttackAction.NONE)
            this.usedWeapon = ItemStack.EMPTY;
        player.yBodyRot = player.yHeadRot;
        if (this.currentAction.onStart != null)
            this.currentAction.onStart.accept(player);
        if (packet && player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CWeaponUse(this.currentAction, this.usedWeapon), serverPlayer);
        }
    }

    public void tick(PlayerData data, Player player) {
        if (this.currentAnim != null) {
            if (this.currentAnim.tick()) {
                this.setAnimationBasedOnState(player, AttackAction.NONE, false, null);
            } else {
                if (!player.level.isClientSide) {
                    boolean changedItem = player.getMainHandItem() != this.usedWeapon;
                    if (changedItem && this.usedWeapon.getItem() instanceof ItemSpell) {
                        for (int i = 0; i < data.getInv().getContainerSize(); i++) {
                            if (data.getInv().getItem(i) == this.usedWeapon) {
                                changedItem = false;
                                break;
                            }
                        }
                    }
                    if (changedItem) {
                        this.setAnimationBasedOnState(player, AttackAction.NONE, true, null);
                    }
                }
                if (this.currentAnim != null && this.weaponConsumer != null)
                    this.weaponConsumer.handle(player, this.usedWeapon, data, this.currentAnim);
            }
        } else {
            if (--this.timeFrame <= 0) {
                this.count = 0;
                if (!player.level.isClientSide && this.chainTrackerAction.nextAction != null)
                    this.doWeaponAttack(player, this.chainTrackerAction.nextAction.apply(player, this), player.getMainHandItem(), null);
                else
                    this.chainTrackerAction = AttackAction.NONE;
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

    public AnimatedAction getCurrentAnim() {
        return this.currentAnim;
    }

    public AnimatedAction getFadingAnim() {
        return this.fadingAnim;
    }
}
