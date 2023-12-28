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
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RailStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.RAIL_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.4);
        if (anim.isAtTick(0.12))
            handler.setMoveTargetDir(dir.scale(-5).add(0, 1.1, 0), anim, 0.76);
        if (anim.isAtTick(0.76))
            handler.setMoveTargetDir(dir.scale(-3).add(0, -1.1, 0), anim, 1.28);
        if (anim.isAtTick(0.64)) {
            double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            dir = dir.normalize().scale(range);
            List<LivingEntity> entites = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1).expandTowards(dir),
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
                    CombatUtils.knockBackEntity(entity, target, 1.1f);
            }
            CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
        }
    }
}
