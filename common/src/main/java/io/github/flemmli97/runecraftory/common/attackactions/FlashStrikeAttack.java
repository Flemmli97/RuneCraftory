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
import net.minecraft.world.phys.Vec3;

public class FlashStrikeAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.FLASH_STRIKE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        handler.store(DataKey.FIXED_LOOK, true);
        if (state.isAt("move_1")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, 0.3, 0));
            playSound(entity, RuneCraftorySounds.SPELL_GENERIC_LEAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.7f);
        }
        if (state.isAt("move_2")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.7));
        }
        if (state.isAt("move_end")) {
            handler.applyDelta(entity.getDeltaMovement().scale(0.9));
            handler.store(DataKey.MOVE_DIRECTION, null);
        }
        if (!entity.level().isClientSide) {
            if (state.isAt("reset"))
                handler.resetHitEntityTracker();
            if (state.isPast("attack_start") && !state.isPast("attack_end")) {
                double range = Math.max(0, CombatUtils.getRange(entity, -1));
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox()
                                .inflate(1, 0, 0).expandTowards(0, 0, range)))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.FLASH_STRIKE))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.2f))
                        .executeAttack());
            }
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return true;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
