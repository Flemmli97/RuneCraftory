package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.api.Spell;
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

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeaponHandler {

    private static final float FADE_TICK = 3;

    private AttackAction currentAction = ModAttackActions.NONE.get();
    private int chainCount;

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

    public boolean doWeaponAttack(LivingEntity entity, AttackAction action, ItemStack stack) {
        return this.doWeaponAttack(entity, action, stack, null, false);
    }

    public boolean doWeaponAttack(LivingEntity entity, AttackAction action, ItemStack stack, @Nullable Spell spell, boolean ignoreCurrent) {
        if (entity.level.isClientSide || this.canExecuteAction(entity, action, true, ignoreCurrent)) {
            action.onSetup(entity, this);
            this.spell = spell;
            this.usedWeapon = stack;
            this.setAnimationBasedOnState(entity, action, true);
            return true;
        }
        return false;
    }

    public boolean canExecuteAction(LivingEntity entity, AttackAction action) {
        return this.canExecuteAction(entity, action, true, false);
    }

    public boolean canExecuteAction(LivingEntity entity, AttackAction action, boolean allowNone, boolean ignoreCurrent) {
        if (allowNone && (this.currentAction == ModAttackActions.NONE.get() || this.currentAnim == null))
            return true;
        if (!ignoreCurrent && !this.currentAction.canOverride(entity, this) && !this.isCurrentAnimationDone())
            return false;
        if (this.currentAction != action)
            return true;
        return this.chainCount < action.attackChain(entity, this.chainCount).maxChains();
    }

    private void setAnimationBasedOnState(LivingEntity entity, AttackAction action, boolean packet) {
        AttackAction change = this.currentAction.onChange(entity, this);
        if (change != null)
            action = change;
        this.moveDir = null;
        if (action == ModAttackActions.NONE.get()) {
            this.resetStates();
        }
        this.lastAnim = this.currentAnim;
        this.timeSinceLastChange = 0;
        this.currentAction = action;
        int chain = this.getChainCount();
        this.currentAnim = action.getAnimation(entity, chain);
        if (this.currentAction != ModAttackActions.NONE.get()) {
            this.chainCount++;
        } else
            this.usedWeapon = ItemStack.EMPTY;
        entity.yBodyRot = entity.yHeadRot;
        this.resetHitEntityTracker();
        this.lockLook = false;
        this.currentAction.onStart(entity, this);
        this.consumeSpellOnStart = false;
        if (!entity.level.isClientSide) {
            if (entity instanceof IAnimated animated && this.currentAnim != null) {
                animated.getAnimationHandler().setAnimation(this.currentAnim);
            }
            if (packet) {
                Platform.INSTANCE.sendToTrackingAndSelf(new S2CWeaponUse(this.currentAction, this.usedWeapon, chain, entity), entity);
            }
        }
    }

    public void clientSideUpdate(LivingEntity entity, AttackAction action, ItemStack stack, int count) {
        if (!entity.level.isClientSide)
            return;
        this.chainCount = count;
        this.setAnimationBasedOnState(entity, action, false);
        this.usedWeapon = stack;
    }

    private void resetStates() {
        this.spell = null;
        this.chainCount = 0;
        this.toolUseData = null;
        this.hitEntityTracker.clear();
        this.target = null;
    }

    public void tick(LivingEntity entity) {
        if (this.currentAnim != null) {
            if (this.currentAnim.tick(1 + (int) (this.currentAnim.getSpeed() * this.currentAction.attackChain(entity, this.chainCount).chainFrameTime()))) {
                this.setAnimationBasedOnState(entity, ModAttackActions.NONE.get(), false);
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
                        this.setAnimationBasedOnState(entity, ModAttackActions.NONE.get(), true);
                    }
                }
                this.currentAction.run(entity, this.usedWeapon, this, this.currentAnim);
            }
        }
        if (this.moveDir != null) {
            entity.setDeltaMovement(this.moveDir);
            this.moveDuration--;
            if (this.moveDuration <= 0)
                this.moveDir = null;
        }
        this.timeSinceLastChange++;
    }

    private boolean isCurrentAnimationDone() {
        return this.currentAnim != null && this.currentAnim.isPastTick(1 + this.currentAnim.getLength());
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

    public void setChainCount(int count) {
        this.chainCount = count;
    }

    public int getChainCount() {
        return this.chainCount;
    }

    public boolean isMovementBlocked() {
        return this.currentAction.disableMovement(this.currentAnim);
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

    public void addHitEntityTracker(List<LivingEntity> list) {
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
