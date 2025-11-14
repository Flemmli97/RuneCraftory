package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
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
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.STORM.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isAt("attack") && handler.getComboCount() != 5) {
            if (!entity.level().isClientSide) {
                double range = CombatUtils.getRange(entity, 0) * 0.5;
                if (handler.getComboCount() == 3) {
                    range *= 2;
                }
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, range, 0.5f, false))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.STORM))
                        .executeAttack();
            }
            playSound(entity, RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        switch (handler.getComboCount()) {
            case 1 -> {
                if (state.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.3).add(0, 0.1, 0));
                }
            }
            case 2 -> {
                if (state.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.35));
                }
            }
            case 3 -> {
                if (state.isAt("move")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.2));
                }
            }
            case 4 -> {
                if (state.isAt("up")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.2).add(0, 0.05, 0));
                }
                if (state.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.2).add(0, -0.05, 0));
                }
            }
            case 5 -> {
                if (state.isAt("up")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.25).add(0, 0.15, 0));
                }
                if (state.isAt("down")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.applyDelta(dir.scale(0.3).add(0, -0.35, 0));
                }
                entity.fallDistance = 0;
                if (state.isAt("attack")) {
                    if (!entity.level().isClientSide) {
                        CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1, 0.5, 0)
                                        .expandTowards(0, -1, CombatUtils.getRange(entity, 0))))
                                .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.STORM))
                                .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.1f))
                                .executeAttack();
                    }
                    playSound(entity, RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        super.onStart(entity, handler);
        handler.store(DataKey.GRAVITY, entity.isNoGravity());
        entity.setNoGravity(true);
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler<?> handler) {
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
