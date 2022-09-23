package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TeleportSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        if (entity instanceof Mob mob && mob.hasRestriction()) {
            Vec3 home = Vec3.atCenterOf(mob.getRestrictCenter());
            if (mob.distanceToSqr(home) > 100) {
                this.safeTeleportTo(mob, home.x(), home.y(), home.z());
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
                    player.respawn();
                    player.changeDimension(serverLevel);
                    this.safeTeleportTo(player, home.x(), home.y(), home.z());
                    return true;
                }
            }
            this.safeTeleportTo(player, home.x(), home.y(), home.z());
            return true;
        }
        return false;
    }

    private void safeTeleportTo(Entity entity, double x, double y, double z) {
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
    }

    @Override
    public int rpCost() {
        return 0;
    }
}
