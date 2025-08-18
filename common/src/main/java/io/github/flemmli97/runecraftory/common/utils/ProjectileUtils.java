package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import io.github.flemmli97.tenshilib.common.entity.BeamEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class ProjectileUtils {

    public static void shoot(LivingEntity shooter, AdvancedProjectile projectile, float velocity, float inaccuracy) {
        Vec3 target = getAimTarget(shooter, projectile.position());
        if (target != null) {
            projectile.shootAtPosition(target.x(), target.y(), target.z(), velocity, inaccuracy);
        } else
            projectile.shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0, velocity, inaccuracy);
    }

    public static void shoot(LivingEntity shooter, BeamEntity beam, float inaccuracy) {
        TargetPosition target;
        if (shooter instanceof MobAttackExt ext && (target = ext.getTargetPosition()) != null) {
            Vec3 v = target.asVec(beam.position());
            beam.setRotationTo(v.x(), v.y(), v.z(), inaccuracy);
        } else if (shooter instanceof Mob mob && mob.getTarget() != null) {
            beam.setRotationTo(mob.getTarget(), inaccuracy);
        } else {
            beam.setYRot((shooter.yHeadRot - 180) % 360.0F);
            beam.setXRot(shooter.getXRot() % 360.0F);
        }
    }

    public static Vec3 getAimTarget(LivingEntity shooter) {
        return getAimTarget(shooter, shooter.getEyeHeight() - 0.1);
    }

    /**
     * Use for when projectile has a non default offset
     *
     * @param from The projectiles position
     */
    public static Vec3 getAimTarget(LivingEntity shooter, Vec3 from) {
        return getAimTarget(shooter, from.y() - shooter.getY());
    }

    public static Vec3 getAimTarget(LivingEntity shooter, double offset) {
        TargetPosition target;
        if (shooter instanceof MobAttackExt ext && (target = ext.getTargetPosition()) != null) {
            return target.asVec(shooter.position().add(0, offset, 0));
        } else if (shooter instanceof Mob mob && mob.getTarget() != null) {
            return EntityUtils.getStraightProjectileTarget(shooter.position(), mob.getTarget());
        }
        return null;
    }
}
