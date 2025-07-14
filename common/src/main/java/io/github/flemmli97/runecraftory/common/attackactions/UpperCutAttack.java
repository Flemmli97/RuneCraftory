package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class UpperCutAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.UPPER_CUT, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack")) {
            entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            if (!entity.level().isClientSide) {
                AABB aabb = new AABB(-1, -0.02, 0, 1, entity.getBbHeight() + 1, entity.getBbWidth() + 2);
                OrientedBoundingBox obb = new OrientedBoundingBox(aabb, entity.getYRot(), 0, entity.position());
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(obb))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .doOnSuccess(e -> e.setDeltaMovement(e.getDeltaMovement().add(0, 0.8, 0)))
                        .executeAttack();
            }
        }
    }
}
