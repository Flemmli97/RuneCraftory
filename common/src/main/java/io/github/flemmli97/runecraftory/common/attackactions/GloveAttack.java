package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GloveAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.GLOVES.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack() && handler.getChainCount() != 5) {
            if (handler.getChainCount() != 4)
                CombatUtils.attack(entity, stack);
            else
                CombatUtils.EntityAttack.create(entity,
                                CombatUtils.EntityAttack.aabbTargets(new AABB(-1, -1, -1, 1, 1, 1).move(entity.position().add(0, 0.2, 0)
                                        .add(entity.getDeltaMovement().normalize().scale(0.4)))))
                        .executeAttack();
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setMoveTargetDir(dir.scale(0.15), anim, anim.getTick());
                }
            }
            case 2, 3 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setMoveTargetDir(dir.scale(0.12), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.14)) {
                    handler.setMoveTargetDir(dir.scale(3).add(0, -3, 0), anim, 0.44);
                    entity.resetFallDistance();
                }
                if (anim.isAtTick(0.04)) {
                    handler.setMoveTargetDir(dir.scale(1).add(0, 3, 0), anim, 0.14);
                }
                if (anim.isPastTick(0.40) && entity.isOnGround()) {
                    handler.clearMoveTarget();
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.01));
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setSpinStartRot(entity.getYRot());
                    handler.resetHitEntityTracker();
                    Vec3 dir2 = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1)).scale(6);
                    handler.setMoveTargetDir(dir2.add(0, 2.5, 0), anim, 0.68);
                }
                if (anim.isAtTick(0.68)) {
                    Vec3 dir2 = CombatUtils.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1)).scale(4);
                    handler.setMoveTargetDir(dir2.add(0, -2.5, 0), anim, 1.12);
                }
                if (anim.isPastTick(0.16) && !anim.isPastTick(1.12)) {
                    entity.resetFallDistance();
                }
                if (!entity.level.isClientSide && anim.isPastTick(0.2) && !anim.isPastTick(1.08)) {
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity,
                                    CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.5)))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
        if (handler.getChainCount() == 5) {
            handler.lockLook(anim.isPastTick(0.08) && !anim.isPastTick(1.2));
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 5 && entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.gloveUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 5;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.FIST, 20) ? 5 : 4, chain == 5 ? 0 : 8);
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        if (handler.getCurrentAnim() == null)
            return null;
        if (handler.getChainCount() == 5 && handler.getCurrentAnim().isPastTick(0.24) && !handler.getCurrentAnim().isPastTick(1.04))
            return Pose.SPIN_ATTACK;
        return null;
    }
}
