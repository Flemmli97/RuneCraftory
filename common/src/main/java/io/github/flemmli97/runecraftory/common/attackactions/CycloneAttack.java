package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
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

public class CycloneAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.CYCLONE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (!state.isPast("attack_start") || state.isPast("attack_end")) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
            entity.xxa = 0;
            entity.zza = 0;
        }
        if (state.isAt("attack_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot() + 170);
            handler.resetHitEntityTracker();
            entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        if (state.isAt("reset")) {
            handler.resetHitEntityTracker();
            entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        if (!entity.level().isClientSide) {
            CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("attack_start", 0), state.getMarker("attack_end", 0),
                    handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) - 360 * 4.5f, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.CYCLONE))
                        .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.5f))
                        .executeAttack());
            }
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return true;
    }

    @Override
    public float movementReduction(WeaponHandler<?> handler) {
        return 1;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
