package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DashSlashAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DASH_SLASH.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getChainCount() == 2) {
            handler.clearMoveTarget();
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.95, 1, 0.95));
            if (anim.canAttack()) {
                if (!entity.level.isClientSide) {
                    OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(-entity.getBbWidth(), 0, 0, entity.getBbWidth(), 1, entity.getBbWidth() + 1)
                            .inflate(0.3), entity.getYRot(), 0, entity.position());
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(obb))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1))
                            .executeAttack();
                }
                entity.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        } else {
            handler.lockLook(true);
            if (anim.isPastTick(0.2)) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.5).add(0, 0.3, 0), anim, 0.28);
                } else if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(5), anim, anim.getLength());
                }
                if (anim.isAtTick(0.32))
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level.isClientSide && !anim.isPastTick(0.72)) {
                    double range = CombatUtils.getRange(entity, -1);
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(range * 0.5, 0, 0)
                                    .expandTowards(0, 0, range)))
                            .withBonusAttributes(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() != 1)
            return;
        Vec3 mot = entity.getDeltaMovement();
        double lenHor = mot.x * mot.x + mot.z * mot.z;
        entity.setDeltaMovement(mot.multiply(lenHor > 0.5 ? 0.5 : 1, 1, lenHor > 0.5 ? 0.5 : 1));
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(2, 0);
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        AnimatedAction anim = handler.getCurrentAnim();
        return anim != null && handler.getChainCount() == 1 && anim.isPastTick(0.36);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 1;
    }
}
