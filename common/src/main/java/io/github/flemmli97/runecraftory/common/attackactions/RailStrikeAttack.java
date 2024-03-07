package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class RailStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.RAIL_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(-0.4);
        if (anim.isAtTick(0.12))
            handler.setMoveTargetDir(dir.scale(5).add(0, 1.1, 0), anim, 0.76);
        if (anim.isAtTick(0.76))
            handler.setMoveTargetDir(dir.scale(3).add(0, -1.1, 0), anim, 1.28);
        if (anim.getTickRaw() % (3 * anim.getSpeed()) == 0) {
            handler.resetHitEntityTracker();
            if (!anim.isPastTick(1))
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        if (!entity.level.isClientSide && anim.isPastTick(0.24) && !anim.isPastTick(1.08)) {
            double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            dir = dir.normalize().scale(range);
            handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1).expandTowards(dir)))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.1f))
                    .executeAttack());
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }
}
