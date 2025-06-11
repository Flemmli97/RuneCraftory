package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RailStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.RAIL_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(-0.4);
        if (anim.isAt("move_1"))
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.9).add(0, 0.17, 0));
        if (anim.isAt("move_2"))
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.6).add(0, -0.17, 0));
        if (anim.isAt("move_end"))
            handler.store(DataKey.MOVE_DIRECTION, null);
        if (anim.isAt("reset")) {
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        handler.applyMoveDirection();
        if (!entity.level().isClientSide && anim.isPast("attack_start") && !anim.isPast("attack_end")) {
            double range = CombatUtils.getRange(entity, 0);
            handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1, 0.3, range * 0.7)
                            .expandTowards(0, 0, -range * 0.3)))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.1f))
                    .executeAttack());
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return true;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
