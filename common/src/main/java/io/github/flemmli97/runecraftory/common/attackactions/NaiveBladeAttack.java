package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class NaiveBladeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (chain == 1)
            return PlayerModelAnimations.NAIVE_BLADE_SUCCESS.create(speed);
        return PlayerModelAnimations.NAIVE_BLADE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getChainCount() == 2) {
            if (anim.isAtTick(0.48)) {
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                handler.setMoveTargetDir(new Vec3(0, 1, 0), anim, 0.76);
            }
            if (anim.isAtTick(0.76)) {
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                handler.setMoveTargetDir(new Vec3(0, -1, 0), anim, 0.96);
            }
            if (!entity.level.isClientSide) {
                if (anim.canAttack()) {
                    float reach = (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) + 0.5f;
                    List<LivingEntity> entities = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(reach, 1, reach), (t) -> t != entity && !t.isAlliedTo(entity) && t.isPickable() &&
                            inReach(entity.position(), t, reach));
                    entity.swing(InteractionHand.MAIN_HAND, true);
                    if (entity instanceof ServerPlayer player)
                        player.sweepAttack();
                    CombatUtils.applyTempAttributeMult(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
                    for (LivingEntity target : entities) {
                        boolean flag = false;
                        if (entity instanceof Player player)
                            flag = CombatUtils.playerAttackWithItem(player, target, false, false);
                        else if (entity instanceof Mob mob)
                            flag = mob.doHurtTarget(target);
                        if (flag)
                            CombatUtils.knockBackEntity(entity, target, 1.3f);
                    }
                    CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
                }
                if (anim.isAtTick(0.96)) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 25), 1.5f))
                            .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                            .executeAttack();
                    entity.swing(InteractionHand.MAIN_HAND, true);
                }
            }
        } else if (anim.canAttack()) {
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHAIN_PLACE, entity.getSoundSource(), 1.5f, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }


    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        if (handler.getChainCount() == 2) {
            entity.playSound(ModSounds.SPELL_NAIVE_BLADE.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(2, 0);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 2;
    }

    public static boolean canCounter(WeaponHandler handler) {
        AnimatedAction anim = handler.getCurrentAnim();
        return handler.getCurrentAction() instanceof NaiveBladeAttack
                && anim != null && handler.getChainCount() == 1 && anim.isPastTick(0.12) && !anim.isPastTick(0.72);
    }

    private static boolean inReach(Vec3 origin, Entity entity, float reach) {
        double dX = entity.getX() - origin.x;
        double dY = entity.getY() - origin.y();
        if (dY < -0.75 || dY > 1.15)
            return false;
        dY = Math.abs(dY) + 0.75;
        double dZ = entity.getZ() - origin.z;
        reach += entity.getBbWidth() * 0.5;
        return dX * dX + dY * dY + dZ * dZ <= reach * reach;
    }
}
