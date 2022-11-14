package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.network.S2CWeaponUse;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class PlayerWeaponHandler {

    //We need +1 for the length cause vanilla models dont reset all their values everytime
    public static final AnimatedAction shortSwordUse = new AnimatedAction(16 + 1, 6, "short_sword");
    public static final AnimatedAction hammerAxeUse = new AnimatedAction(20 + 1, 12, "hammer_axe");
    public static final AnimatedAction longSwordUse = new AnimatedAction(16 + 1, 5, "long_sword");
    public static final AnimatedAction dualBladeUse = new AnimatedAction(19 + 1, 7, "dual_blades");
    public static final AnimatedAction staffUse = new AnimatedAction(16 + 1, 8, "staff");

    //Weapon and ticker
    private int fireballSpellFlag, bigFireballSpellFlag;
    private int spellTicker;
    private int ticker = 0;
    private PlayerData.WeaponSwing weapon;
    private int swings, timeSinceLastSwing;
    //Gloves charge
    private int gloveTick = -1;
    private ItemStack glove = ItemStack.EMPTY;
    //Spear charge
    private int spearUseCounter = 0;
    private int spearTicker = 0;

    private int toolAxeHammerCounter, toolAxeHammerTicker, toolAxeHammerCharge;
    private ItemStack axeHammerTool;

    private WeaponUseState weaponUseState;
    private AnimatedAction currentAnim;
    private ItemStack usedWeapon;
    private Runnable weaponRunnable;
    private Consumer<AnimatedAction> weaponConsumer;

    public void tick(PlayerData data, Player player) {
        --this.gloveTick;
        if (this.gloveTick == 0)
            player.maxUpStep -= 0.5;
        if (player instanceof ServerPlayer serverPlayer) {
            if (--this.spellTicker == 0) {
                this.fireballSpellFlag = 0;
                this.bigFireballSpellFlag = 0;
            }
            this.updateGlove(data, serverPlayer);
            --this.spearTicker;
            --this.toolAxeHammerTicker;
            if (this.toolAxeHammerTicker == 0 || (this.axeHammerTool != null && player.getMainHandItem() != this.axeHammerTool)) {
                this.resetAxeHammerUse();
            }
        }
        this.ticker = Math.max(--this.ticker, 0);
        this.timeSinceLastSwing = Math.max(--this.timeSinceLastSwing, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
        }
        if (this.currentAnim != null) {
            if (this.currentAnim.tick())
                this.setAnimationTo(null);
            else {
                if (!player.level.isClientSide) {
                    if (player.getMainHandItem() != this.usedWeapon) {
                        this.setAnimationBasedOnState(player, WeaponUseState.NONE);
                    } else {
                        if (this.weaponRunnable != null && this.currentAnim.canAttack()) {
                            this.weaponRunnable.run();
                        }
                    }
                }
                if (this.currentAnim != null && this.weaponConsumer != null)
                    this.weaponConsumer.accept(this.currentAnim);
            }
        }
    }

    public int fireballSpellFlag() {
        return this.fireballSpellFlag;
    }

    public void setFireballSpellFlag(int flag, int resetTime) {
        this.fireballSpellFlag = flag;
        this.spellTicker = resetTime;
    }

    public int bigFireballSpellFlag() {
        return this.bigFireballSpellFlag;
    }

    public void setBigFireballSpellFlag(int flag, int resetTime) {
        this.bigFireballSpellFlag = flag;
        this.spellTicker = resetTime;
    }

    public int animationTick() {
        return this.ticker;
    }

    public void startAnimation(int tick) {
        this.ticker = tick;
    }

    public boolean canStartGlove() {
        return this.gloveTick <= 0;
    }

    public void startGlove(Player player, ItemStack stack) {
        this.gloveTick = 50;
        this.glove = stack;
        player.maxUpStep += 0.5;
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CWeaponUse(PlayerWeaponHandler.WeaponUseState.GLOVERIGHTCLICK), serverPlayer);
        }
    }

    public int getGloveTick() {
        return this.gloveTick;
    }

    private void updateGlove(PlayerData data, ServerPlayer player) {
        if (this.gloveTick <= 0) {
            return;
        }
        Vec3 look = player.getLookAngle();
        Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(player.isOnGround() ? 0.6 : 0.3).add(0, player.getDeltaMovement().y, 0);
        player.setDeltaMovement(move);
        player.hurtMarked = true;
        if (this.gloveTick % 4 == 0) {
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1.0));
            if (!list.isEmpty())
                LevelCalc.useRP(player, data, 5, true, false, true, EnumSkills.FIST);
            for (LivingEntity e : list) {
                if (e != player) {
                    LevelCalc.levelSkill(player, data, EnumSkills.DUAL, 2);
                    CombatUtils.playerAttackWithItem(player, e, this.glove, 0.5f, false, false, false);
                }
            }
        }
    }

    public boolean canStartSpear() {
        return this.spearTicker <= 0 || this.spearUseCounter++ > 20;
    }

    public void startSpear() {
        this.spearTicker = 80;
        this.spearUseCounter = 0;
    }

    public void onUseSpear() {
        this.spearUseCounter++;
    }

    public void startWeaponSwing(PlayerData.WeaponSwing swing, int delay) {
        if (this.weapon != swing) {
            this.swings = 0;
        }
        ++this.swings;
        this.timeSinceLastSwing = delay;
        this.weapon = swing;
    }

    public boolean isAtUltimate() {
        return this.weapon.getMaxSwing() == this.swings;
    }

    public void doWeaponAttack(Player player, WeaponUseState state, ItemStack stack, Runnable attack) {
        this.setAnimationBasedOnState(player, state);
        this.usedWeapon = stack;
        this.weaponRunnable = attack;
    }

    private void setAnimationTo(AnimatedAction anim) {
        if (anim == null) {
            this.weaponRunnable = null;
            this.weaponConsumer = null;
            this.currentAnim = null;
        } else {
            this.currentAnim = anim.create();
        }
    }

    public void setAnimationBasedOnState(Player player, WeaponUseState state) {
        this.weaponUseState = state;
        switch (this.weaponUseState) {
            case NONE -> this.currentAnim = null;
            case SHORTSWORDRIGHTCLICK -> this.setAnimationTo(shortSwordUse);
            case LONGSWORDRIGHTCLICK -> this.setAnimationTo(longSwordUse);
            case SPEARRIGHTCLICK -> {
            }
            case HAMMERAXERIGHTCLICK -> {
                this.setAnimationTo(hammerAxeUse);
                this.weaponConsumer = ItemAxeBase.movePlayer(player);
            }
            case DUALRIGHTCLICK -> this.setAnimationTo(dualBladeUse);
            case STAFFRIGHTCLICK -> this.setAnimationTo(staffUse);
            default -> {
            }
        }
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CWeaponUse(this.weaponUseState), serverPlayer);
        }
        player.yBodyRot = player.yHeadRot;
    }

    public AnimatedAction getCurrentAnim() {
        return this.currentAnim;
    }

    public int getCurrentToolCharge() {
        return this.toolAxeHammerCharge;
    }

    public void useAxeOrHammer(ItemStack stack, int charge) {
        this.axeHammerTool = stack;
        this.toolAxeHammerCounter++;
        this.toolAxeHammerCharge = charge;
        this.toolAxeHammerTicker = 15;
        if (this.toolAxeHammerCounter >= 3) {
            this.resetAxeHammerUse();
        }
    }

    private void resetAxeHammerUse() {
        this.toolAxeHammerCounter = 0;
        this.toolAxeHammerCharge = 0;
        this.toolAxeHammerTicker = -1;
        this.axeHammerTool = null;
    }

    public enum WeaponUseState {

        NONE,
        SHORTSWORDRIGHTCLICK,
        LONGSWORDRIGHTCLICK,
        SPEARRIGHTCLICK,
        HAMMERAXERIGHTCLICK,
        DUALRIGHTCLICK,
        GLOVERIGHTCLICK,
        STAFFRIGHTCLICK
    }
}
