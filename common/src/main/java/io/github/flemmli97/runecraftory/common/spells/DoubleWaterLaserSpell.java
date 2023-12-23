package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DoubleWaterLaserSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = 0; i < 2; i++) {
            float posYawOff = (i == 0 ? -1 : 1) * 30;
            Vector3f vec = RayTraceUtils.rotatedAround(entity.getLookAngle(), Vector3f.YP, posYawOff);
            EntityWaterLaser laser = new EntityWaterLaser(level, entity);
            Vec3 pre = laser.position();
            laser.setPos(laser.getX() + vec.x(), laser.getY() + vec.y(), laser.getZ() + vec.z());
            laser.setMaxTicks(entity instanceof Player ? 44 : 15);
            laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
            laser.setPositionYawOffset(posYawOff);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                LivingEntity target = mob.getTarget();
                Vec3 targetPos = EntityUtil.getStraightProjectileTarget(pre.add(0, vec.y(), 0), target);
                laser.setRotationTo(targetPos.x(), targetPos.y(), targetPos.z(), 0);
            }
            level.addFreshEntity(laser);
        }
        return true;
    }
}
