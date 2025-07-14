package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class LongSwordAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), Skills.LONGSWORD, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.LONG_SWORD.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (handler.getComboCount() != 4) {
            if (!entity.level().isClientSide && anim.isAt("attack")) {
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                CombatUtils.getRange(entity, 0),
                                CombatUtils.getWidth(entity, 0), 0.5)))
                        .executeAttack();
            }
            if (anim.isAt("attack")) {
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
            }
            if (anim.isAt("step")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                entity.setDeltaMovement(dir.scale(0.5));
            }
        } else {
            if (anim.isAt("spin_start")) {
                handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                handler.resetHitEntityTracker();
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
            }
            if (anim.isAt("reset")) {
                handler.resetHitEntityTracker();
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
            }
            CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                    handler.get(DataKey.SPIN_ROTATION) + 150, handler.get(DataKey.SPIN_ROTATION) - 500, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        if (handler.getComboCount() == 4 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(Platform.INSTANCE.getPlayerData(player), GeneralConfig.longSwordUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return handler.getComboCount() == 4;
    }

    @Override
    public float movementReduction(AnimationState current) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return targetCombo != 4;
    }
}
