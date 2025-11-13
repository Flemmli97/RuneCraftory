package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GloveUseAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        return AttackAction.create(PlayerModelAnimations.GLOVES_USE, 1);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isPast("attack_start") && !state.isPast("attack_end")) {
            Vec3 move = EntityUtils.horizontalLookAngle(entity).scale(entity.onGround() ? 0.5 : 0.3).add(0, entity.getDeltaMovement().y, 0);
            handler.applyDelta(move);
            if (state.isAt("reset"))
                handler.resetHitEntityTracker();
            if (!entity.level().isClientSide) {
                List<LivingEntity> hit = new ArrayList<>();
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox()
                                .inflate(1)))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(hit::add)
                        .executeAttack());
                if (!hit.isEmpty() && entity instanceof ServerPlayer serverPlayer) {
                    LevelCalc.levelSkill(RunecraftoryAttachments.PLAYER_DATA.get().get(serverPlayer), Skills.DUAL, 2);
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        entity.getAttribute(Attributes.STEP_HEIGHT)
                .addTransientModifier(new AttributeModifier(LibConstants.STEP_UP_TEMP, 0.5, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler<?> handler) {
        entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(LibConstants.STEP_UP_TEMP);
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.matches(state -> state.isPast("attack_start") && !state.isPast("attack_end")))
            return Pose.SPIN_ATTACK;
        return null;
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
