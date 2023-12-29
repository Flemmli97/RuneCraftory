package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WindSlashAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.WIND_SLASH.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        handler.lockLook(true);
        if (handler.getChainCount() == 1) {
            if (anim.isAtTick(0.12)) {
                handler.setSpinStartRot(entity.getYRot());
                handler.resetHitEntityTracker();
            }
            if (anim.isAtTick(0.64)) {
                handler.resetHitEntityTracker();
            }
            if (anim.isAtTick(0.12)) {
                Vec3 dir = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(1).add(0, 0.9, 0), anim, 0.32);
            }
            if (anim.isAtTick(0.32)) {
                Vec3 dir = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(5), anim, 1.12);
            }
            if (anim.isAtTick(1.20)) {
                Vec3 dir = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(1).add(0, -0.9, 0), anim, 1.36);
            }
            if (!entity.level.isClientSide && anim.isPastTick(0.2) && !anim.isPastTick(1.08))
                handler.addHitEntityTracker(CombatUtils.attackInAABB(entity, entity.getBoundingBox().inflate(0.75), e -> !handler.getHitEntityTracker().contains(e),
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null));
        } else {
            if (anim.isAtTick(0.44)) {
                handler.resetHitEntityTracker();
            }
            if (anim.isAtTick(0.84)) {
                Vec3 dir = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(1).add(0, -0.9, 0), anim, 1.12);
            }
            if (!entity.level.isClientSide && !anim.isPastTick(0.88))
                handler.addHitEntityTracker(CombatUtils.attackInAABB(entity, entity.getBoundingBox().inflate(0.75), e -> !handler.getHitEntityTracker().contains(e),
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null));
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        if (handler.getChainCount() == 2) {
            handler.setSpinStartRot(entity.getYRot());
            handler.resetHitEntityTracker();
            Vec3 dir = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(5), handler.getCurrentAnim(), 0.84);
        }
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        return (handler.getCurrentAnim().isPastTick(0.96) && !handler.getCurrentAnim().isPastTick(1.20));
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(2, 0);
    }
}
