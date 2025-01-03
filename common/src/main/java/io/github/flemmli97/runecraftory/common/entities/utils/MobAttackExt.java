package io.github.flemmli97.runecraftory.common.entities.utils;

import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface MobAttackExt {

    TargetPosition getTargetPosition();

    default boolean reversed() {
        return false;
    }

    record TargetPosition(Vec3 position, double minHeight, double maxHeight) {

        public static TargetPosition of(LivingEntity target) {
            Vec3 pos = target.position();
            return new TargetPosition(pos, pos.y(), pos.y() + target.getBbHeight());
        }

        public static TargetPosition of(Vec3 target) {
            return new TargetPosition(target, target.y(), target.y());
        }

        public Vec3 asVec(Vec3 from) {
            return EntityUtils.getStraightProjectileTarget(from, this.position, this.minHeight, this.maxHeight);
        }
    }
}
