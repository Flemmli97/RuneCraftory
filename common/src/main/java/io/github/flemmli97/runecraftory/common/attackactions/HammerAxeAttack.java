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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HammerAxeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.HAMMER_AXE.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.canAttack() && handler.getChainCount() != 3) {
            AttackAction.attack(entity, stack);
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        if (handler.getChainCount() == 3) {
            if (anim.isAtTick(0.12)) {
                handler.setSpinStartRot(entity.getYRot());
                handler.resetHitEntityTracker();
            }
            if (anim.isAtTick(0.64)) {
                handler.resetHitEntityTracker();
            }
            if (anim.isPastTick(0.12) && !anim.isPastTick(1.28)) {
                Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1));
                if (anim.isAtTick(0.12))
                    handler.setMoveTargetDir(dir.scale(3).add(0, 2, 0), anim, 0.76);
                if (anim.isAtTick(0.76))
                    handler.setMoveTargetDir(dir.scale(3).add(0, -2, 0), anim, 1.28);
                entity.resetFallDistance();
                handler.addHitEntityTracker(CombatUtils.attackInAABB(entity, entity.getBoundingBox().inflate(0.5), e -> !handler.getHitEntityTracker().contains(e)));
            } else
                handler.clearMoveTarget();
        }
        if (handler.getChainCount() == 3) {
            handler.lockLook(anim.isPastTick(0.12) && !anim.isPastTick(1.28));
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 3 && entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.hammerAxeUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 3;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(AttackAction.canPerform(entity, EnumSkills.HAMMERAXE, 20) ? 3 : 2, chain == 3 ? 0 : 8);
    }
}
