package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class TeleportSpell extends Spell {

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        if (entity instanceof Mob mob && mob.level instanceof ServerLevel) {
            if (mob.hasRestriction()) {
                Vec3 home = Vec3.atCenterOf(mob.getRestrictCenter());
                if (mob.distanceToSqr(home) > 100) {
                    safeTeleportTo(mob, home.x(), home.y(), home.z());
                    return true;
                }
            }
            Optional<GlobalPos> mem;
            if (mob.getBrain().hasMemoryValue(MemoryModuleType.HOME) && (mem = mob.getBrain().getMemory(MemoryModuleType.HOME)).isPresent()) {
                Vec3 home = Vec3.atCenterOf(mem.get().pos());
                ResourceKey<Level> levelKey = mem.get().dimension();
                if (mob.level.dimension() != levelKey) {
                    ServerLevel serverLevel = mob.getServer().getLevel(levelKey);
                    if (serverLevel != null) {
                        changeDimension(mob, serverLevel, home.x(), home.y(), home.z());
                        return true;
                    }
                }
                safeTeleportTo(mob, home.x(), home.y(), home.z());
                return true;
            }
        }
        if (entity instanceof ServerPlayer player) {
            Vec3 home;
            ResourceKey<Level> levelKey;
            if (player.getRespawnPosition() != null) {
                home = Vec3.atCenterOf(player.getRespawnPosition());
                levelKey = player.getRespawnDimension();
                if (player.level.dimension() == levelKey && player.distanceToSqr(home) <= 100) {
                    home = Vec3.atCenterOf(player.getServer().overworld().getSharedSpawnPos());
                    levelKey = player.getServer().overworld().dimension();
                }
            } else {
                home = Vec3.atCenterOf(player.getServer().overworld().getSharedSpawnPos());
                levelKey = player.getServer().overworld().dimension();
            }
            if (player.level.dimension() != levelKey) {
                ServerLevel serverLevel = player.getServer().getLevel(levelKey);
                if (serverLevel != null) {
                    changeDimension(player, serverLevel, home.x(), home.y(), home.z());
                    return true;
                }
            }
            safeTeleportTo(player, home.x(), home.y(), home.z());
            return true;
        }
        return false;
    }

    public static void safeTeleportTo(Entity entity, double x, double y, double z) {
        AABB oldBox = entity.getBoundingBox();
        entity.teleportTo(x, y, z);
        entity.resetFallDistance();
        while (!entity.level.noCollision(entity) && entity.getY() < entity.level.getMaxBuildHeight()) {
            entity.setPos(entity.getX(), entity.getY() + 1.0, entity.getZ());
        }
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);
        if (entity.level instanceof ServerLevel serverLevel)
            for (int i = 0; i < 32; ++i) {
                serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + serverLevel.random.nextDouble() * 2.0, entity.getZ(), 0, serverLevel.random.nextGaussian(), 0.0, serverLevel.random.nextGaussian(), 1);
            }
        if (entity instanceof ServerPlayer player)
            teleportNearbyImportantEntities(player, player.getLevel(), oldBox, x, y, z);
    }

    public static void changeDimension(Entity entity, ServerLevel newLevel, double x, double y, double z) {
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        if (entity instanceof ServerPlayer player) {
            player.stopRiding();
            AABB oldBB = player.getBoundingBox();
            ServerLevel oldLvl = player.getLevel();
            player.teleportTo(newLevel, x, y, z, yaw, pitch);
            if (player.isSleeping()) {
                player.stopSleepInBed(true, true);
            }
            teleportNearbyImportantEntities(player, oldLvl, oldBB, x, y, z);
        } else {
            entity.unRide();
            Entity old = entity;
            entity = old.getType().create(newLevel);
            if (entity != null) {
                entity.restoreFrom(old);
                entity.moveTo(x, y, z, yaw, pitch);
                old.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
                newLevel.addDuringTeleport(entity);
            } else
                return;
        }
        while (!newLevel.noCollision(entity) && entity.getY() < newLevel.getMaxBuildHeight()) {
            entity.setPos(entity.getX(), entity.getY() + 1.0, entity.getZ());
        }
        newLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);
        for (int i = 0; i < 32; ++i) {
            newLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + newLevel.random.nextDouble() * 2.0, entity.getZ(), 0, newLevel.random.nextGaussian(), 0.0, newLevel.random.nextGaussian(), 1);
        }
    }

    private static void teleportNearbyImportantEntities(ServerPlayer player, ServerLevel oldLevel, AABB oldBox, double x, double y, double z) {
        boolean crossDim = player.getLevel().dimension() != oldLevel.dimension();
        for (Entity e : oldLevel.getEntities(EntityTypeTest.forClass(Mob.class), oldBox.inflate(24), e -> {
            if (e instanceof BaseMonster monster)
                return player.getUUID().equals(monster.getOwnerUUID()) && monster.behaviourState() == BaseMonster.Behaviour.FOLLOW;
            if (e instanceof EntityNPCBase npc)
                return player.getUUID().equals(npc.getEntityToFollowUUID());
            return false;
        })) {
            if (crossDim)
                changeDimension(e, player.getLevel(), x + oldLevel.random.nextDouble() * 2 - 1, y, z + oldLevel.random.nextDouble() * 2 - 1);
            else
                safeTeleportTo(e, x + oldLevel.random.nextDouble() * 2 - 1, y, z + oldLevel.random.nextDouble() * 2 - 1);
        }
    }
}
