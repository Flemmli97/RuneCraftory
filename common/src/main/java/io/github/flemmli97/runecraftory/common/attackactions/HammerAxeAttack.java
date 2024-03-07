package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
            CombatUtils.attack(entity, stack);
        }
        if (handler.getChainCount() == 3) {
            if (anim.isAtTick(0.12)) {
                handler.setSpinStartRot(entity.getYRot());
                handler.resetHitEntityTracker();
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 0.7f, 0.5f);
            }
            if (anim.isAtTick(0.64)) {
                handler.resetHitEntityTracker();
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 1, 0.7f);
            }
            if (anim.isPastTick(0.12) && !anim.isPastTick(1.28)) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                if (anim.isAtTick(0.12))
                    handler.setMoveTargetDir(dir.scale(3).add(0, 2, 0), anim, 0.76);
                if (anim.isAtTick(0.76))
                    handler.setMoveTargetDir(dir.scale(3).add(0, -2, 0), anim, 1.28);
                entity.resetFallDistance();
                if (!entity.level.isClientSide)
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity,
                                    CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1)))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .withAttackSound(SoundEvents.PLAYER_ATTACK_STRONG)
                            .executeAttack());
            } else
                handler.clearMoveTarget();
            handler.lockLook(anim.isPastTick(0.12) && !anim.isPastTick(1.28));
        } else if (anim.isAtTick(0.4)) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
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
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.HAMMERAXE, 20) ? 3 : 2, chain == 3 ? 0 : 8);
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return !GeneralConfig.allowMoveOnAttack.get() && super.disableMovement(current);
    }
}
