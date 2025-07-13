package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class StormAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 0)
            .addCombo(ComboContainer.past("done"), 0)
            .addCombo(ComboContainer.past("done"), 0)
            .addCombo(ComboContainer.past("done"), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.STORM.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack") && handler.getComboCount() != 5) {
            if (!entity.level().isClientSide) {
                double range = CombatUtils.getRange(entity, 0) * 0.5;
                if (handler.getComboCount() == 3) {
                    range *= 2;
                }
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, range, 0.5f, false))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .executeAttack();
            }
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        switch (handler.getComboCount()) {
            case 1 -> {
                if (anim.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.2).add(0, 0.1, 0));
                }
            }
            case 2 -> {
                if (anim.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.3));
                }
            }
            case 3 -> {
                if (anim.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.2));
                }
            }
            case 4 -> {
                if (anim.isAt("up")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.15).add(0, 0.05, 0));
                }
                if (anim.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.15).add(0, -0.05, 0));
                }
            }
            case 5 -> {
                if (anim.isAt("up")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.2).add(0, 0.15, 0));
                }
                if (anim.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    entity.setDeltaMovement(dir.scale(0.25).add(0, -0.35, 0));
                }
                handler.applyMoveDirection();
                entity.fallDistance = 0;
                if (anim.isAt("attack")) {
                    if (!entity.level().isClientSide) {
                        CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1, 0.5, 0)
                                        .expandTowards(0, -1, CombatUtils.getRange(entity, 0))))
                                .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                                .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.1f))
                                .executeAttack();
                    }
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        super.onStart(entity, handler);
        handler.store(DataKey.GRAVITY, entity.isNoGravity());
        entity.setNoGravity(true);
    }

    @Override
    public void onEnd(LivingEntity entity, AttackActionHandler handler) {
        handler.clearWith(DataKey.GRAVITY, entity::setNoGravity);
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
