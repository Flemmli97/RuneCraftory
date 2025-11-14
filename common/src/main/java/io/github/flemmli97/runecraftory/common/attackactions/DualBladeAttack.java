package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
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
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), Skills.DUAL, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.DUAL_BLADES.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (handler.getComboCount() != 5 && handler.getComboCount() != 6 && handler.getComboCount() != 8) {
            if (state.isAt("attack")) {
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                    CombatUtils.getRange(entity, 0),
                                    CombatUtils.getWidth(entity, handler.getComboCount() == 7 ? 1 : 0),
                                    0.5)))
                            .executeAttack();
                }
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        }
        switch (handler.getComboCount()) {
            case 1 -> {
                if (state.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.1));
                }
            }
            case 2 -> {
                if (state.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.3));
                }
            }
            case 3 -> {
                if (state.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.35));
                }
            }
            case 4 -> {
                if (state.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.4));
                }
            }
            case 5 -> {
                if (state.isAt("step")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.4));
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("spin_start", 0), state.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 6 -> {
                if (state.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot() - 90);
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.5));
                }
                if (state.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("spin_start", 0), state.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
                attack = spinAttack(entity, state, state.getMarker("spin_start", 0), state.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION) + 180, handler.get(DataKey.SPIN_ROTATION) + 180 + 360, 0);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            case 7 -> {
                if (state.isAt("leap")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(1.7).add(0, 0.7, 0));
                }
                if (state.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(1).add(0, -0.2, 0));
                }
            }
            case 8 -> {
                if (state.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot() + 120);
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (state.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (state.isAt("last"))
                    entity.playSound(RuneCraftorySounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.5f);
                CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("spin_start", 0), state.getMarker("spin_end", 0),
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
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.getComboCount() != 8 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(RunecraftoryAttachments.PLAYER_DATA.get().get(player), GeneralConfig.dualBladeUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return handler.getComboCount() == 8;
    }

    @Override
    public float movementReduction(WeaponHandler<?> handler) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
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
