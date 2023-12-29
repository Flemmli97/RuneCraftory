package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DualBladeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DUAL_BLADES.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack() && handler.getChainCount() != 5 && handler.getChainCount() != 6) {
            CombatUtils.attack(entity, stack);
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.35), anim, anim.getTick());
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.15), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.35), anim, anim.getTick());
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.2), anim, anim.getTick());
                    handler.setSpinStartRot(entity.getYRot() - 20);
                    handler.resetHitEntityTracker();
                }
                if (anim.isAtTick(0.4)) {
                    handler.resetHitEntityTracker();
                }
                if (!entity.level.isClientSide && anim.isPastTick(0.2)) {
                    int start = Mth.ceil(0.2 * 20.0D);
                    int end = anim.getLength();
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = 360 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                }
            }
            case 6 -> {
                if (anim.isAtTick(0.08)) {
                    handler.setSpinStartRot(entity.getYRot() - 90);
                    handler.resetHitEntityTracker();
                }
                if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(0.7), anim, anim.getTick());
                    handler.resetHitEntityTracker();
                }
                if (!entity.level.isClientSide && anim.isPastTick(0.12)) {
                    int start = Mth.ceil(0.12 * 20.0D);
                    int end = anim.getLength();
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = 360 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + 180 + f * angleInc), (rot + 180 + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                }
            }
            case 7 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.9).add(0, -1.5, 0), anim, anim.getLength());
                } else if (anim.isAtTick(0.08)) {
                    handler.setMoveTargetDir(dir.scale(0.75).add(0, 1.5, 0), anim, 0.2);
                }
            }
            case 8 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setSpinStartRot(entity.getYRot() + 160);
                    handler.resetHitEntityTracker();
                }
                if (anim.isAtTick(0.48) || anim.isAtTick(0.68) || anim.isAtTick(0.92)) {
                    handler.resetHitEntityTracker();
                }
                if (!entity.level.isClientSide && anim.isPastTick(0.24) && !anim.isPastTick(1.12)) {
                    int start = Mth.ceil(0.28 * 20.0D);
                    int end = Mth.ceil(1.12 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -1440 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 8 && entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.dualBladeUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 8;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        int frame = switch (chain) {
            case 5, 6, 7 -> 4;
            case 8 -> 0;
            default -> 8;
        };
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.DUAL, 20) ? 8 : 7, frame);
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        AnimatedAction anim = handler.getCurrentAnim();
        if (anim == null)
            return true;
        return switch (handler.getChainCount()) {
            case 5, 6, 7 -> anim.isPastTick(0.24);
            default -> false;
        };
    }
}