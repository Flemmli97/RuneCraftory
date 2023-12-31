package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class GustAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.GUST.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        handler.lockLook(true);
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.4);
        if (anim.isAtTick(0.12))
            handler.setMoveTargetDir(dir.scale(6).add(0, 2.5, 0), anim, 0.56);
        if (anim.isAtTick(0.56))
            handler.setMoveTargetDir(dir.scale(7).add(0, -2.5, 0), anim, 0.76);
        if (!entity.level.isClientSide && anim.isAtTick(0.52)) {
            double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            dir = dir.normalize().scale(range);
            handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1).expandTowards(dir)))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.1f))
                    .executeAttack());
        }
        handler.lockLook(true);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }
}
