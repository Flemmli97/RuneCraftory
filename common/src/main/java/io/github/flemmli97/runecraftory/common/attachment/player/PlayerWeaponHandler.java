package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PlayerWeaponHandler {

    //Weapon and ticker
    private int fireballSpellFlag, bigFireballSpellFlag;
    private int spellTicker;
    private int ticker = 0;
    private PlayerData.WeaponSwing weapon;
    private int swings, timeSinceLastSwing;
    //Gloves charge
    private int gloveTick;
    private ItemStack glove = ItemStack.EMPTY;
    //Spear charge
    private int spearUseCounter = 0;
    private int spearTicker = 0;

    public void tick(PlayerData data, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (--this.spellTicker == 0) {
                this.fireballSpellFlag = 0;
                this.bigFireballSpellFlag = 0;
            }
            this.updateGlove(data, serverPlayer);
            --this.spearTicker;
        }
        this.ticker = Math.max(--this.ticker, 0);
        this.timeSinceLastSwing = Math.max(--this.timeSinceLastSwing, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
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

    public void startGlove(ItemStack stack) {
        this.gloveTick = 35;
        this.glove = stack;
    }

    private void updateGlove(PlayerData data, ServerPlayer player) {
        if (this.gloveTick <= 0)
            return;
        --this.gloveTick;
        Vec3 look = player.getLookAngle();
        Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(0.8).add(0, player.getDeltaMovement().y, 0);
        player.setDeltaMovement(move);
        player.hurtMarked = true;
        if (this.gloveTick % 4 == 0) {
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1.0));
            if (!list.isEmpty())
                LevelCalc.useRP(player, data, 5, true, false, true, 1, EnumSkills.FIST);
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
}
