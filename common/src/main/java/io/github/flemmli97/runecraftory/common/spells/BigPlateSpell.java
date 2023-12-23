package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BigPlateSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 pos = null;
        if (entity instanceof Mob mob && mob.getTarget() != null)
            pos = mob.getTarget().position();
        if (entity instanceof Player) {
            TargetingConditions target = TargetingConditions.forCombat();
            target.selector(e -> {
                if (e instanceof OwnableEntity ownable && e.getUUID().equals(ownable.getOwnerUUID()))
                    return false;
                return !(e instanceof EntityNPCBase npc) || !e.getUUID().equals(npc.getEntityToFollowUUID());
            });
            LivingEntity nearest = level.getNearestEntity(LivingEntity.class, target, entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().inflate(16));
            if (nearest != null)
                pos = nearest.position();
        }
        if (pos == null) {
            Vec3 horizontalLook = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z()).normalize();
            pos = entity.position().add(horizontalLook);
        }
        EntityBigPlate plate = new EntityBigPlate(level, entity);
        plate.setPos(pos.x(), pos.y() + 6, pos.z());
        plate.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.25f));
        level.addFreshEntity(plate);
        return true;
    }
}
