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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(0.4), anim, anim.getTick());
                }
            }
            case 2 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.4), anim, anim.getTick());
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.2), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.5).add(0, 0.9, 0), anim, anim.getLength());
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.04)) {
                    handler.setMoveTargetDir(new Vec3(0, -0.9, 0), anim, 0.2);
                }
            }
            case 6 -> {
                if (anim.isAtTick(0.24)) {
                    handler.setSpinStartRot(entity.getYRot() + 30);
                    handler.resetHitEntityTracker();
                }
                if (anim.isAtTick(0.48) || anim.isAtTick(0.72)) {
                    handler.resetHitEntityTracker();
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
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f, e -> !handler.getHitEntityTracker().contains(e)));
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 6 && entity instanceof ServerPlayer player)
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
}
