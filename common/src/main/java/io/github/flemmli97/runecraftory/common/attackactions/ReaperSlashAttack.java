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

public class ReaperSlashAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.REAPER_SLASH, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isAt("attack_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot() - 80);
        }
        if (state.isAt("attack_middle"))
            entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        if (!entity.level().isClientSide) {
            CombatUtils.EntityAttack attack = spinAttack(entity, state, state.getMarker("attack_start", 0), state.getMarker("attack_end", 0),
                    handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 170, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.REAPER_SLASH))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }
}
