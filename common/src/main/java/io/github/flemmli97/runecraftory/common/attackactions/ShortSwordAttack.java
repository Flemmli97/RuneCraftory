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

public class ShortSwordAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), Skills.SHORTSWORD, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.SHORT_SWORD.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (handler.getComboCount() != 6) {
            if (state.isAt("attack")) {
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                    CombatUtils.getRange(entity, 0),
                                    CombatUtils.getWidth(entity, 0), 0.5)))
                            .executeAttack();
                }
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getComboCount()) {
            case 1, 2 -> {
                if (state.isAt("step")) {
                    handler.applyDelta(dir.scale(0.35));
                }
            }
            case 3 -> {
                if (state.isAt("step")) {
                    handler.applyDelta(dir.scale(0.25));
                }
            }
            case 4 -> {
                if (state.isAt("step")) {
                    handler.applyDelta(dir.scale(0.35).add(0, 0.4, 0));
                }
            }
            case 5 -> {
                if (state.isAt("step")) {
                    handler.applyDelta(new Vec3(0, -0.4, 0));
                }
            }
            case 6 -> {
                if (state.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                    handler.resetHitEntityTracker();
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (state.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (state.isAt("spin_start")) {
                    handler.store(DataKey.MOVE_DIRECTION, new Vec3(0, 0.1, 0));
                }
                if (state.isAt("spin_end")) {
                    handler.applyDelta(new Vec3(0, -0.1, 0));
                    handler.store(DataKey.MOVE_DIRECTION, null);
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("spin_start", 0), state.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION) + 30, handler.get(DataKey.SPIN_ROTATION) - 1100, 0);
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
        if (handler.getComboCount() == 6 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(RunecraftoryAttachments.PLAYER_DATA.get().get(player), GeneralConfig.shortSwordUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return handler.getComboCount() == 6;
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
        return targetCombo != 6;
    }
}
