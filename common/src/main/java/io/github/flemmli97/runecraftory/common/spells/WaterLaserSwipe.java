package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WaterLaserSwipe extends Spell {

    private final int duration;
    private final float angle;

    public WaterLaserSwipe(int duration, float angle) {
        this.duration = duration;
        this.angle = angle;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        float motion = (this.angle * 2) / this.duration;
        motion = entity instanceof MobAttackExt ext && ext.reversed() ? motion : -motion;
        EntityWaterLaser laser = new EntityWaterLaser(level, entity, motion).setMaxTicks(this.duration);
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            dir = mob.getTarget().getEyePosition(1).subtract(laser.position());
        } else {
            dir = entity.getLookAngle();
        }
        float offset = entity instanceof MobAttackExt ext && ext.reversed() ? -this.angle : this.angle;
        laser.setRotationToDirWithOffset(dir.x(), dir.y(), dir.z(), 0, offset);
        level.addFreshEntity(laser);
        return true;
    }
}