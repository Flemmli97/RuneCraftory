package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
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

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), EnumSkills.DUAL, 20), 0)
            .build();

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DUAL_BLADES.get(comboIdx).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (handler.getComboCount() != 5 && handler.getComboCount() != 6 && handler.getComboCount() != 8) {
            if (anim.isAt("attack")) {
                if (!entity.level.isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(IAOEWeapon.createOBB(entity, stack,
                                    CombatUtils.getRange(entity, 0),
                                    CombatUtils.getWidth(entity, handler.getComboCount() == 7 ? 1 : 0))))
                            .executeAttack();
                }
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        }
        switch (handler.getComboCount()) {
            case 1 -> {
                if (anim.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.15));
                }
            }
            case 2 -> {
                if (anim.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.2));
                }
            }
            case 3 -> {
                if (anim.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.25));
                }
            }
            case 4 -> {
                if (anim.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.3));
                }
            }
            case 5 -> {
                if (anim.isAt("step")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.3));
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 6 -> {
                if (anim.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot() - 90);
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.3));
                }
                if (anim.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
                attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION) + 180, handler.get(DataKey.SPIN_ROTATION) + 180 + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 7 -> {
                if (anim.isAt("leap")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(1.1).add(0, 0.6, 0));
                }
                if (anim.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.9).add(0, -0.4, 0));
                }
            }
            case 8 -> {
                if (anim.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot() + 120);
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAt("last"))
                    entity.playSound(ModSounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.5f);
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) - 4 * 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        if (handler.getComboCount() != 8 && entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.dualBladeUltimate, true, 0, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return handler.getComboCount() == 8;
    }

    @Override
    public float movementReduction(AnimatedAction current) {
        return GeneralConfig.moveSpeedAttack.get().floatValue();
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return targetCombo < 5;
    }
}
