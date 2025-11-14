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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GloveAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), Skills.FIST, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.GLOVES.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isAt("attack") && handler.getComboCount() != 5) {
            if (!entity.level().isClientSide) {
                if (handler.getComboCount() != 4)
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                    CombatUtils.getRange(entity, 0),
                                    CombatUtils.getWidth(entity, 0), 0.5)))
                            .executeAttack();
                else
                    CombatUtils.EntityAttack.create(entity,
                                    CombatUtils.EntityAttack.aabbTargets(new AABB(-1, -1, -1, 1, 1.5, 1.5).move(entity.position().add(0, 0.2, 0)
                                            .add(entity.getDeltaMovement().normalize().scale(0.4)))))
                            .executeAttack();
            }
            entity.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        switch (handler.getComboCount()) {
            case 1, 2, 3 -> {
                if (state.isAt("step")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.3));
                }
            }
            case 4 -> {
                if (state.isAt("jump")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(1.5).add(0, 1.1, 0));
                }
                if (state.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.9).add(0, -0.3, 0));
                }
                entity.resetFallDistance();
            }
            case 5 -> {
                if (state.isAt("leap")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                    handler.resetHitEntityTracker();
                    Vec3 dir = CombatUtils.fromRelativeVector(handler.get(DataKey.SPIN_ROTATION), new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(2.7).add(0, 0.6, 0));
                }
                entity.resetFallDistance();
                if (state.isAt("attack_start"))
                    entity.playSound(RuneCraftorySounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.3f);
                if (!entity.level().isClientSide && state.isPast("attack_start") && !state.isPast("attack_end")) {
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity,
                                    CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.5)))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
        if (handler.getComboCount() == 5) {
            handler.store(DataKey.FIXED_LOOK, state.isPast("move_start") && !state.isPast("move_end"));
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.getComboCount() == 5 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(RunecraftoryAttachments.PLAYER_DATA.get().get(player), GeneralConfig.gloveUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return handler.getComboCount() == 5;
    }

    @Override
    public float movementReduction(WeaponHandler<?> handler) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.getComboCount() == 5 && handler.matches(state -> state.isPast("attack_start") && !state.isPast("attack_end")))
            return Pose.SPIN_ATTACK;
        return null;
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return targetCombo < 4;
    }
}
