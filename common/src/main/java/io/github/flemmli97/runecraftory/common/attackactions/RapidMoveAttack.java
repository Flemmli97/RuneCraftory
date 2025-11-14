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
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RapidMoveAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.RAPID_MOVE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        Entity target = handler.get(DataKey.TARGET);
        if (target != null) {
            Vec3 dir = target.position().subtract(entity.position());
            double width = 0.5 * entity.getBbWidth();
            double targetWidth = 0.5 * target.getBbWidth();
            double closeDist = width * width + targetWidth * targetWidth;
            closeDist += 1;
            if (dir.lengthSqr() < closeDist) {
                handler.applyDelta(Vec3.ZERO);
            } else {
                Vec3 motion = dir.normalize().scale(1 + entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2);
                if (dir.lengthSqr() < closeDist * 2)
                    motion = dir.scale(0.1);
                handler.applyDelta(motion);
            }
            Vec3 direct = EntityUtils.getStraightProjectileTarget(entity.getEyePosition(), target);
            entity.lookAt(EntityAnchorArgument.Anchor.EYES, direct);
            entity.hurtMarked = true;
        }
        if (state.isAt("attack")) {
            playSound(entity, RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            if (!entity.level().isClientSide) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.5)
                                .expandTowards(0, 0, 1)))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.RAPID_MOVE))
                        .executeAttack());
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        super.onStart(entity, handler);
        if (!entity.level().isClientSide()) {
            LivingEntity target = entity.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), entity, entity.getX(),
                    entity.getY(), entity.getZ(), entity.getBoundingBox().inflate(20, 10, 20));
            if (target != null)
                handler.store(DataKey.TARGET, target);
        }
    }
}
