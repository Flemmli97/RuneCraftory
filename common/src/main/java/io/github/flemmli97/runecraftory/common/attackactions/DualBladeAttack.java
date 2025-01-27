package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.server.level.ServerPlayer;
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
            CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(IAOEWeapon.createOBB(entity, stack,
                            CombatUtils.getRange(entity, 0),
                            CombatUtils.getWidth(entity, 0))))
                    .executeAttack();
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.25), anim, anim.getTick());
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.15), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.3), anim, anim.getTick());
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.25), anim, anim.getTick());
                    handler.setSpinStartRot(entity.getYRot());
                    handler.resetHitEntityTracker();
                }
                if (anim.isAtTick(0.4)) {
                    handler.resetHitEntityTracker();
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.16, 0.28,
                        handler.getSpinStartRot(), handler.getSpinStartRot() + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 6 -> {
                if (anim.isAtTick(0.08)) {
                    handler.setSpinStartRot(entity.getYRot() - 90);
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(0.5), anim, anim.getTick());
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.16, 0.28,
                        handler.getSpinStartRot(), handler.getSpinStartRot() + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
                attack = spinAttack(entity, anim, 0.16, 0.28,
                        handler.getSpinStartRot() + 180, handler.getSpinStartRot() + 180 + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 7 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.8).add(0, -1.5, 0), anim, anim.getLength());
                } else if (anim.isAtTick(0.08)) {
                    handler.setMoveTargetDir(dir.scale(0.55).add(0, 1.5, 0), anim, 0.2);
                }
            }
            case 8 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setSpinStartRot(entity.getYRot() + 120);
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.48) || anim.isAtTick(0.68) || anim.isAtTick(0.92)) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.92))
                    entity.playSound(ModSounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.5f);

                CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.24, 1.12,
                        handler.getSpinStartRot(), handler.getSpinStartRot() - 4 * 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() != 8) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        } else if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.dualBladeUltimate, true, 0, false));
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
    public float movementReduction(AnimatedAction current) {
        return GeneralConfig.moveSpeedAttack.get().floatValue();
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
