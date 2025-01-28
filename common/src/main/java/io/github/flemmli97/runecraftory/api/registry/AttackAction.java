package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;

public class AttackAction extends CustomRegistryEntry<AttackAction> {

    public static CombatUtils.EntityAttack spinAttack(LivingEntity entity, AnimatedAction anim, double startSec, double endSec, float startRot, float endRot, float range) {
        if (!entity.level.isClientSide() && anim.isPastTick(startSec) && !anim.isPastTick(endSec)) {
            int start = (int) Math.ceil(startSec * 20);
            int end = (int) Math.ceil(endSec * 20);
            float len = (end - start) / anim.getSpeed();
            float f = (anim.getTick() - start) / anim.getSpeed();
            float inc = (endRot - startRot) / len;
            return CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((startRot + f * inc), (startRot + (f + 1) * inc), range));
        }
        return null;
    }

    public static CombatUtils.EntityAttack spinAttack(LivingEntity entity, AnimatedAction anim, double startSec, double endSec, float startRot, float endRot,
                                                      CombatUtils.FloatMap xRot, float range) {
        if (!entity.level.isClientSide() && anim.isPastTick(startSec) && !anim.isPastTick(endSec)) {
            int start = (int) Math.ceil(startSec * 20);
            int end = (int) Math.ceil(endSec * 20);
            float len = (end - start) / anim.getSpeed();
            float f = (anim.getTick() - start) / anim.getSpeed();
            float inc = (endRot - startRot) / len;
            CombatUtils.FloatMap xRot2 = p -> xRot.get((p + f) / len);
            return CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((startRot + f * inc), (startRot + (f + 1) * inc), xRot2, range));
        }
        return null;
    }

    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        return null;
    }

    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {

    }

    public void onSetup(LivingEntity entity, WeaponHandler handler) {

    }

    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.consumeSpellOnStart() && entity.getLevel() instanceof ServerLevel serverLevel) {
            entity.swing(InteractionHand.MAIN_HAND);
            ItemStack stack = handler.getUsedWeapon();
            if (handler.getSpellToCast() != null) {
                Spell spell = handler.getSpellToCast();
                if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                    spell.levelSkill(player);
                }
            }
        }
    }

    public AttackAction onChange(LivingEntity entity, WeaponHandler handler) {
        this.onEnd(entity, handler);
        return null;
    }

    public void onEnd(LivingEntity entity, WeaponHandler handler) {
    }

    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return false;
    }

    public boolean disableItemSwitch() {
        return true;
    }

    public float movementReduction(AnimatedAction current) {
        return 0;
    }

    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        return null;
    }

    public ComboContainer combos() {
        return null;
    }

    public enum OverrideType {
        NONE,
        SCHEDULE,
        REPLACE
    }
}
