package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class DeltaStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DELTA_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.28) || anim.isAtTick(0.88)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.33), anim, anim.getTick());
        }
        if (!entity.level.isClientSide) {
            if (anim.canAttack() || anim.isAtTick(0.48) || anim.isAtTick(0.88)) {
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 15), 1))
                        .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                        .executeAttack();
                entity.swing(InteractionHand.MAIN_HAND, true);
            }
        }
    }
}
