package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
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

public class RushAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (chain < 6)
            chain = 0;
        else
            chain = 1;
        return PlayerModelAnimations.RUSH_ATTACK.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getChainCount() == 7) {
            if (anim.isAtTick(0.28)) {
                Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(3).add(0, -1.5, 0), anim, 0.4);
            }
            entity.fallDistance = 0;
            if (anim.canAttack()) {
                CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 10), 0.5f, null,
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), e -> CombatUtils.knockBackEntity(entity, e, 0.8f));
                entity.swing(InteractionHand.MAIN_HAND, true);
            }
        } else {
            if (anim.isAtTick(0.32) || anim.isAtTick(0.48)) {
                Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(0.2), anim, anim.getTick());
            }
            if (anim.isAtTick(0.92)) {
                Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(0.5).add(0, 1.5, 0), anim, 1.4);
            }
            entity.fallDistance = 0;
            if (anim.canAttack() || anim.isAtTick(0.52) || anim.isAtTick(1.08)) {
                CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 10), 0.5f, null,
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), anim.isAtTick(1.08) ? e -> e.setDeltaMovement(e.getDeltaMovement().add(0.0, 0.4f, 0.0)) : null);
                entity.swing(InteractionHand.MAIN_HAND, true);
            }
        }
    }

    @Override
    public void onSetup(LivingEntity entity, WeaponHandler handler) {
        if (handler.getCurrentAnim() != null && handler.getChainCount() < 7 && handler.getCurrentAnim().isPastTick(1.12))
            handler.setChainCount(6);
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        return switch (handler.getChainCount()) {
            case 1, 2, 3, 4, 5 -> (!handler.getCurrentAnim().isPastTick(0.92) && handler.getCurrentAnim().isPastTick(0.6)) || handler.getCurrentAnim().isPastTick(1.12);
            case 6 -> handler.getCurrentAnim().isPastTick(1.12);
            default -> false;
        };
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(7, 0);
    }
}
