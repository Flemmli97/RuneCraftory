package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AxelDisasterAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.AXEL_DISASTER, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isAt("move_1")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, 0.5, 0));
            entity.playSound(RuneCraftorySounds.SPELL_GENERIC_LEAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        }
        if (state.isAt("move_2")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5));
        }
        if (state.isAt("move_3")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, -0.5, 0));
        }
        if (state.isAt("move_done")) {
            handler.store(DataKey.MOVE_DIRECTION, null);
        }
        if (state.isPast("attack_start") && !state.isPast("attack_end")) {
            if (!entity.level().isClientSide) {
                if (state.isAt("reset"))
                    handler.resetHitEntityTracker();
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox()
                                .inflate(0.75).expandTowards(0, 0, 0.5)))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.AXEL_DISASTER))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return true;
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.matches(state -> state.isPast("move_1") && !state.isPast("move_done")))
            return Pose.SPIN_ATTACK;
        return null;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
