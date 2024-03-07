package io.github.flemmli97.runecraftory.common.world;

import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NPCSpawner implements CustomSpawner {

    private int cooldown = 1200;

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!level.getServer().isSpawningAnimals() || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
            return 0;
        if (--this.cooldown > 0) {
            return 0;
        }
        int cooldown = level.getRandom().nextInt(MobConfig.NPC_SPAWN_RATE_MAX - MobConfig.NPC_SPAWN_RATE_MIN) + MobConfig.NPC_SPAWN_RATE_MIN;
        if (this.doSpawning(level)) {
            this.cooldown = cooldown;
            return 1;
        }
        this.cooldown = (int) (cooldown * 0.5);
        return 0;
    }

    private boolean doSpawning(ServerLevel level) {
        Player player = level.getRandomPlayer();
        if (player == null || player.isSpectator())
            return false;
        BlockPos blockPos = player.blockPosition();
        for (int i = 0; i < 10; ++i) {
            float f = level.random.nextFloat() * Mth.PI * 2;
            int x = blockPos.getX() + Mth.floor(Mth.cos(f) * 32.0f);
            int z = blockPos.getZ() + Mth.floor(Mth.sin(f) * 32.0f);
            Vec3 pos = this.findRandomSpawnPos(level, new BlockPos(x, blockPos.getY(), z));
            if (pos == null) continue;
            this.trySpawn(level, pos.x, pos.y, pos.z);
            return true;
        }
        return false;
    }

    private void trySpawn(ServerLevel level, double x, double y, double z) {
        EntityNPCBase npc = new EntityNPCBase(ModEntities.NPC.get(), level);
        npc.finalizeSpawn(level, level.getCurrentDifficultyAt(npc.blockPosition()), MobSpawnType.EVENT, null, null);
        npc.moveTo(x, y, z, level.random.nextFloat() * 360.0f, 0.0f);
        level.addFreshEntityWithPassengers(npc);
    }

    @Nullable
    private Vec3 findRandomSpawnPos(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 10; ++i) {
            int x = pos.getX() + level.random.nextInt(16) - 8;
            int z = pos.getZ() + level.random.nextInt(16) - 8;
            BlockPos blockPos = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
            if (!level.isVillage(blockPos) || level.getEntities(EntityTypeTest.forClass(EntityNPCBase.class), new AABB(blockPos).inflate(48), e -> true).size() > 3)
                continue;
            return Vec3.atBottomCenterOf(blockPos);
        }
        return null;
    }
}
