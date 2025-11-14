package io.github.flemmli97.runecraftory.api.registry.action;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;

public class AttackAction {

    public static CombatUtils.EntityAttack spinAttack(LivingEntity entity, AnimationState anim, double startSec, double endSec, float startRot, float endRot, float range) {
        if (!entity.level().isClientSide() && anim.isBetween(startSec, endSec)) {
            float start = (float) (startSec * 20);
            float end = (float) (endSec * 20);
            float f = (float) anim.progress(start, end, 1, 0);
            float fNext = (float) anim.progress(start, end, 1, 1);
            float add = endRot - startRot;
            return CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(startRot + f * add, startRot + fNext * add, range));
        }
        return null;
    }

    public static CombatUtils.EntityAttack spinAttack(LivingEntity entity, AnimationState anim, double startSec, double endSec, float startRot, float endRot,
                                                      CombatUtils.FloatMap xRot, float range) {
        if (!entity.level().isClientSide() && anim.isBetween(startSec, endSec)) {
            float start = (float) (startSec * 20);
            float end = (float) (endSec * 20);
            float f = (float) anim.progress(start, end, 1, 0);
            float fNext = (float) anim.progress(start, end, 1, 1);
            float add = endRot - startRot;
            CombatUtils.FloatMap xRot2 = p -> xRot.get(f + (fNext - f) * p);
            return CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(startRot + f * add, startRot + fNext * add, xRot2, range));
        }
        return null;
    }

    public static AnimationState create(String animation, double speed) {
        return AnimationState.create(PlayerModelAnimations.ANIMS.get(animation), speed);
    }

    public static void playSound(Entity entity, SoundEvent event, float volume, float pitch) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, entity.getSoundSource(), volume, pitch);
    }

    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        return null;
    }

    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {

    }

    public void onSetup(LivingEntity entity, WeaponHandler<?> handler) {

    }

    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        Spell spell = handler.get(DataKey.USED_SPELL);
        if (spell != null && !spell.delayedUse() && entity.level() instanceof ServerLevel serverLevel) {
            entity.swing(InteractionHand.MAIN_HAND);
            ItemStack stack = handler.get(DataKey.USED_WEAPON);
            if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                spell.levelSkill(player);
            }
        }
    }

    public AttackAction onChange(LivingEntity entity, WeaponHandler<?> handler) {
        this.onEnd(entity, handler);
        return null;
    }

    public void onEnd(LivingEntity entity, WeaponHandler<?> handler) {
    }

    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return false;
    }

    public boolean disableItemSwitch() {
        return true;
    }

    public float movementReduction(WeaponHandler<?> handler) {
        return 0;
    }

    public Pose getPose(LivingEntity entity, WeaponHandler<?> handler) {
        return null;
    }

    public ComboContainer combos() {
        return null;
    }

    public boolean usableOnMounts(int targetCombo) {
        return true;
    }

    public enum OverrideType {
        NONE,
        CONTINUE,
        START
    }
}
