package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class StormAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.AFTER_ANIM, 4)
            .addCombo(ComboContainer.AFTER_ANIM, 4)
            .addCombo(ComboContainer.AFTER_ANIM, 4)
            .addCombo(ComboContainer.AFTER_ANIM, 4)
            .build();

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STORM.get(comboIdx).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack() && handler.getComboCount() != 5) {
            double range = CombatUtils.getRange(entity, 0) * 0.5;
            if (handler.getComboCount() == 3) {
                range *= 2;
            }
            CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, range, 0.5f, false))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .executeAttack();
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        if (handler.getComboCount() != 5 && anim.isAtTick(0.12))
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        switch (handler.getComboCount()) {
            case 1 -> {
                if (anim.isAtTick(0.08)) {
                    handler.setMoveTargetDir(dir.scale(1.6).add(0, 0.75, 0), anim, anim.getLength());
                }
                if (anim.isAtTick(0.4)) {
                    handler.clearMoveTarget();
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2));
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.04)) {
                    handler.setMoveTargetDir(dir.scale(0.8), anim, anim.getLength());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.4).add(0, -0.05, 0), anim, anim.getTick());
                }
                if (anim.isAtTick(0.4)) {
                    handler.clearMoveTarget();
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2));
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.04))
                    handler.setMoveTargetDir(dir.scale(1.8).add(0, 1.9, 0), anim, 0.36);
                if (anim.isAtTick(0.36)) {
                    handler.setMoveTargetDir(dir.scale(2).add(0, -2.5, 0), anim, 0.6);
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                entity.fallDistance = 0;
                if (!entity.level.isClientSide && anim.canAttack()) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1, 0.5, 0)
                                    .expandTowards(0, -1, CombatUtils.getRange(entity, 0))))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.1f))
                            .executeAttack();
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        handler.setNoGravity(entity);
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler handler) {
        handler.restoreGravity(entity);
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
