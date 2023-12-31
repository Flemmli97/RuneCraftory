package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

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
            if (!entity.level.isClientSide && anim.canAttack()) {
                Vec3 attackPos = entity.position().add(0, 0.2, 0).add(entity.getLookAngle().scale(0.5));
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(new AABB(-0.5, -1, -0.8, 0.8, 1, 0.5).move(attackPos)))
                        .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                        .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1))
                        .executeAttack();
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
                if (!entity.level.isClientSide && !anim.isPastTick(0.72)) {
                    double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
                    dir = dir.normalize().scale(range);
                    List<LivingEntity> entites = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1).expandTowards(dir),
                            target -> target != entity && !handler.getHitEntityTracker().contains(target) && !target.isAlliedTo(entity) && target.isPickable());
                    handler.addHitEntityTracker(entites);
                    CombatUtils.applyTempAttributeMult(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
                    for (LivingEntity entite : entites) {
                        if (entity instanceof Player player)
                            CombatUtils.playerAttackWithItem(player, entite, false, true, false);
                        else if (entity instanceof Mob mob)
                            mob.doHurtTarget(entite);
                    }
                    CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
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
