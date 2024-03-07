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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ShortSwordAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.SHORT_SWORD.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.canAttack() && handler.getChainCount() != 6) {
            CombatUtils.attack(entity, stack);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(0.25), anim, anim.getTick());
                }
            }
            case 2 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.25), anim, anim.getTick());
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.15), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.35).add(0, 0.9, 0), anim, anim.getLength());
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.04)) {
                    handler.setMoveTargetDir(new Vec3(0, -0.8, 0), anim, 0.2);
                }
            }
            case 6 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setSpinStartRot(entity.getYRot() + 30);
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.48) || anim.isAtTick(0.72)) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.24)) {
                    handler.setMoveTargetDir(new Vec3(0, 2, 0), anim, 1.18);
                }
                if (anim.isAtTick(1.18)) {
                    handler.setMoveTargetDir(new Vec3(0, -2, 0), anim, anim.getLength());
                }
                if (!entity.level.isClientSide() && anim.isPastTick(0.24) && !anim.isPastTick(0.96)) {
                    int start = Mth.ceil(0.24 * 20.0D);
                    int end = Mth.ceil(1 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -1080 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() != 6) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        } else if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.shortSwordUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 6;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.SHORTSWORD, 20) ? 6 : 5, chain == 6 ? 0 : 8);
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return !GeneralConfig.allowMoveOnAttack.get() && super.disableMovement(current);
    }
}
