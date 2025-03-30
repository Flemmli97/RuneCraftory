package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GloveUseAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        return PlayerModelAnimations.GLOVES_USE.create(1);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isPast("attack_start") && !handler.getAnimation().isPast("attack_end")) {
            Vec3 look = entity.getLookAngle();
            Vec3 move = new Vec3(look.x, 0.0, look.z).normalize()
                    .scale(entity.isOnGround() ? 0.5 : 0.3).add(0, entity.getDeltaMovement().y, 0);
            entity.setDeltaMovement(move);
            if (anim.isAt("reset"))
                handler.resetHitEntityTracker();
            if (!entity.level.isClientSide) {
                List<LivingEntity> hit = new ArrayList<>();
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox()
                                .inflate(1)))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(hit::add)
                        .executeAttack());
                if (!hit.isEmpty() && entity instanceof ServerPlayer serverPlayer) {
                    Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.DUAL, 2));
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        entity.maxUpStep += 0.5;
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler handler) {
        entity.maxUpStep -= 0.5;
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        if (handler.getAnimation() == null)
            return null;
        if (handler.getAnimation().isPast("attack_start") && !handler.getAnimation().isPast("attack_end"))
            return Pose.SPIN_ATTACK;
        return null;
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }
}
