package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class RapidMoveAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.RAPID_MOVE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getTarget() != null) {
            Vec3 dir = handler.getTarget().position().subtract(entity.position());
            if (dir.lengthSqr() < 0.1 * 0.1)
                dir = dir.scale(0.001);
            else if (dir.lengthSqr() > 0.7 * 0.7)
                dir = dir.normalize().scale(0.7);
            entity.setDeltaMovement(dir);
        }
        Vec3 lookPos = entity.position().add(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
        entity.lookAt(EntityAnchorArgument.Anchor.EYES, lookPos);
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(entity));
            player.connection.send(new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, lookPos.x, lookPos.y, lookPos.z));
        }
        if (anim.canAttack()) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);

            if (!entity.level.isClientSide) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.5).expandTowards(entity.getLookAngle())))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                        .executeAttack());
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        if (!entity.level.isClientSide()) {
            LivingEntity target = entity.level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), entity, entity.getX(),
                    entity.getY(), entity.getZ(), entity.getBoundingBox().inflate(16, 4, 16));
            if (target == null)
                handler.setMoveTargetDir(CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(7), handler.getCurrentAnim(), 0.48);
            else
                handler.setTarget(target);
        }
    }

    @Override
    public boolean hasAnimation() {
        return false;
    }
}
