package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AxelDisasterAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.AXEL_DISASTER.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.24)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.5).add(0, 0.75, 0), anim, 0.36);
        }
        if (anim.isAtTick(0.36)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(9), anim, 0.8);
        }
        if (anim.isAtTick(0.8)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.5).add(0, -0.75, 0), anim, 0.96);
        }
        if (!entity.level.isClientSide && anim.isPastTick(0.28) && !anim.isPastTick(0.88)) {
            if (anim.getTickRaw() % (8 * anim.getSpeed()) == 0)
                handler.resetHitEntityTracker();
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            List<LivingEntity> entites = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.5).expandTowards(dir),
                    target -> target != entity && !handler.getHitEntityTracker().contains(target) && !target.isAlliedTo(entity) && target.isPickable());
            handler.addHitEntityTracker(entites);
            CombatUtils.applyTempAttributeMult(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
            for (LivingEntity target : entites) {
                boolean flag = false;
                if (entity instanceof Player player)
                    flag = CombatUtils.playerAttackWithItem(player, target, false, true, false);
                else if (entity instanceof Mob mob)
                    flag = mob.doHurtTarget(target);
                if (flag)
                    CombatUtils.knockBackEntity(entity, target, 1.2f);
            }
            CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        return Pose.SPIN_ATTACK;
    }
}
