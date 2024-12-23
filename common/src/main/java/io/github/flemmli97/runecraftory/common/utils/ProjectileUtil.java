package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class ProjectileUtil {

    public static void shoot(LivingEntity shooter, EntityProjectile projectile, float velocity, float inaccuracy) {
        shoot(shooter, projectile, velocity, inaccuracy, 0);
    }

    public static void shoot(LivingEntity shooter, EntityProjectile projectile, float velocity, float inaccuracy, float heightMod) {
        Vec3 target = getAimTarget(shooter);
        if (target != null) {
            projectile.shootAtPosition(target.x(), target.y(), target.z(), velocity, inaccuracy);
        } else
            projectile.shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0, velocity, inaccuracy);
    }

    public static void shoot(LivingEntity shooter, EntityBeam beam, float inaccuracy) {
        Vec3 v;
        if (shooter instanceof MobAttackExt ext && (v = ext.getTargetPosition()) != null) {
            beam.setRotationTo(v.x(), v.y(), v.z(), inaccuracy);
        } else if (shooter instanceof Mob mob && mob.getTarget() != null) {
            beam.setRotationTo(mob.getTarget(), inaccuracy);
        } else {
            beam.setYRot((shooter.yHeadRot - 180) % 360.0F);
            beam.setXRot(shooter.getXRot() % 360.0F);
        }
    }

    public static Vec3 getAimTarget(LivingEntity shooter) {
        Vec3 target;
        if (shooter instanceof MobAttackExt ext && (target = ext.getTargetPosition()) != null) {
            return target;
        } else if (shooter instanceof Mob mob && mob.getTarget() != null) {
            return EntityUtil.getStraightProjectileTarget(shooter.position(), mob.getTarget());
        }
        return null;
    }
}
