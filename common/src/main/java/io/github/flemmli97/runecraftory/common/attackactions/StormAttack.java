package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class StormAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STORM.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack() && handler.getChainCount() != 5) {
            CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), Math.min(15, CombatUtils.getAOE(entity, stack, 10)), 0.5f, null,
                    Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null);
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1 -> {
                if (anim.isAtTick(0.08)) {
                    handler.setMoveTargetDir(dir.scale(1.6).add(0, 0.75, 0), anim, anim.getLength());
                }
                if (anim.isAtTick(0.4)) {
                    handler.clearMoveTarget();
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2));
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.04)) {
                    handler.setMoveTargetDir(dir.scale(0.8), anim, anim.getLength());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.16)) {
                    handler.setMoveTargetDir(dir.scale(0.4).add(0, -0.05, 0), anim, anim.getTick());
                }
                if (anim.isAtTick(0.4)) {
                    handler.clearMoveTarget();
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2));
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.04))
                    handler.setMoveTargetDir(dir.scale(1.8).add(0, 1.9, 0), anim, 0.36);
                if (anim.isAtTick(0.36))
                    handler.setMoveTargetDir(dir.scale(2).add(0, -2.5, 0), anim, 0.6);
                entity.fallDistance = 0;
                if (!entity.level.isClientSide && anim.canAttack()) {
                    double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
                    dir = dir.normalize().scale(range);
                    List<LivingEntity> entites = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1).expandTowards(dir),
                            target -> target != entity && !target.isAlliedTo(entity) && target.isPickable());
                    CombatUtils.applyTempAttributeMult(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
                    for (LivingEntity target : entites) {
                        boolean flag = false;
                        if (entity instanceof Player player)
                            flag = CombatUtils.playerAttackWithItem(player, target, false, true, false);
                        else if (entity instanceof Mob mob)
                            flag = mob.doHurtTarget(target);
                        if (flag)
                            CombatUtils.knockBackEntity(entity, target, 1.1f);
                    }
                    CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        handler.setNoGravity(entity);
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler handler) {
        handler.restoreGravity(entity);
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(5, 8);
    }
}
