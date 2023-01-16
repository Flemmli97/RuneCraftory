package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.Random;
import java.util.function.Function;

public class TeleportUtils {

    public static void safeDimensionTeleport(Mob entity, ServerLevel newLevel, BlockPos target) {
        BlockPos blockPos = target;
        BlockPos safe = null;
        for (int i = 0; i < 10; ++i) {
            int x = randomIntInclusive(entity.getRandom(), -3, 3);
            int y = randomIntInclusive(entity.getRandom(), -1, 1);
            int z = randomIntInclusive(entity.getRandom(), -3, 3);
            BlockPos pos = isSafePos(entity, blockPos.offset(x, y, z), s -> true);
            if (pos != null) {
                safe = pos;
                break;
            }
        }
        if (safe == null)
            return;
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        entity.unRide();
        Entity old = entity;
        entity = (Mob) old.getType().create(newLevel);
        if (entity != null) {
            entity.restoreFrom(old);
            entity.moveTo(safe.getX(), safe.getY(), safe.getZ(), yaw, pitch);
            old.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
            newLevel.addDuringTeleport(entity);
        } else
            return;
        newLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);
        for (int i = 0; i < 32; ++i) {
            newLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + newLevel.random.nextDouble() * 2.0, entity.getZ(), 0, newLevel.random.nextGaussian(), 0.0, newLevel.random.nextGaussian(), 1);
        }
    }

    public static boolean tryTeleportAround(Mob entity, Entity target) {
        BlockPos blockPos = target.blockPosition();
        for (int i = 0; i < 10; ++i) {
            int x = randomIntInclusive(entity.getRandom(), -3, 3);
            int y = randomIntInclusive(entity.getRandom(), -1, 1);
            int z = randomIntInclusive(entity.getRandom(), -3, 3);
            BlockPos pos = blockPos.offset(x, y, z);
            if (validTeleportPlace(entity, pos, s -> true)) {
                entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.getYRot(), entity.getXRot());
                entity.getNavigation().stop();
                return true;
            }
        }
        return false;
    }

    public static boolean tryTeleportAround(Mob entity, BlockPos target) {
        for (int i = 0; i < 10; ++i) {
            int x = randomIntInclusive(entity.getRandom(), -3, 3);
            int y = randomIntInclusive(entity.getRandom(), -1, 1);
            int z = randomIntInclusive(entity.getRandom(), -3, 3);
            BlockPos pos = target.offset(x, y, z);
            if (validTeleportPlace(entity, pos, s -> true)) {
                entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.getYRot(), entity.getXRot());
                entity.getNavigation().stop();
                return true;
            }
        }
        return false;
    }

    public static boolean validTeleportPlace(Mob entity, BlockPos pos, Function<BlockState, Boolean> validPos) {
        BlockPos safe = isSafePos(entity, pos, validPos);
        if (safe == null)
            return false;
        return entity.level.noCollision(entity, entity.getBoundingBox().move(safe));
    }

    public static BlockPos isSafePos(Mob entity, BlockPos pos, Function<BlockState, Boolean> validPos) {
        BlockPathTypes blockPathTypes = entity.getNavigation().getNodeEvaluator().getBlockPathType(entity.level, pos.getX(), pos.getY(), pos.getZ());
        if (blockPathTypes == BlockPathTypes.OPEN) {
            if (!entity.isNoGravity())
                return null;
        } else if (blockPathTypes != BlockPathTypes.WALKABLE) {
            return null;
        }
        BlockState blockState = entity.level.getBlockState(pos.below());
        if (!validPos.apply(blockState)) {
            return null;
        }
        return pos.subtract(entity.blockPosition());
    }

    private static int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
