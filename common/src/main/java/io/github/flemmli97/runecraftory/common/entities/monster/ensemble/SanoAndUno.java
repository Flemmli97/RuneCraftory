package io.github.flemmli97.runecraftory.common.entities.monster.ensemble;

import io.github.flemmli97.runecraftory.common.entities.EnsembleMonsters;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySano;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityUno;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SanoAndUno extends EnsembleMonsters {

    public SanoAndUno(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public List<Supplier<? extends EntityType<?>>> entities() {
        return List.of(ModEntities.SANO, ModEntities.UNO);
    }

    @Override
    public void spawnEntities(ServerLevel serverLevel) {
        BlockPos off = new BlockPos(-6, 0, -3);
        Entity e = ModEntities.SANO.get().create(serverLevel, null, null, null, this.blockPosition()
                        .offset(off.rotate(this.rotation)),
                MobSpawnType.SPAWNER, false, false);
        UUID link = Mth.createInsecureUUID();
        if (e instanceof EntitySano sano) {
            sano.setLevel(this.monsterLevel);
            sano.linkUsing(link);
            if (this.restrictRadius != -1)
                sano.restrictTo(this.blockPosition(), this.restrictRadius);
            sano.setXRot(0);
            sano.setYRot(this.rotation.rotate(-45, 360));
            sano.yHeadRot = sano.getYRot();
            sano.yBodyRot = sano.getYRot();
            serverLevel.addFreshEntityWithPassengers(sano);
        }
        off = new BlockPos(6, 0, -3);
        e = ModEntities.UNO.get().create(serverLevel, null, null, null, this.blockPosition()
                        .offset(off.rotate(this.rotation)),
                MobSpawnType.SPAWNER, false, false);
        if (e instanceof EntityUno uno) {
            uno.setLevel(this.monsterLevel);
            uno.linkUsing(link);
            if (this.restrictRadius != -1)
                uno.restrictTo(this.blockPosition(), this.restrictRadius);
            uno.setXRot(0);
            uno.setYRot(this.rotation.rotate(45, 360));
            uno.yHeadRot = uno.getYRot();
            uno.yBodyRot = uno.getYRot();
            serverLevel.addFreshEntityWithPassengers(uno);
        }
    }

    @Override
    public boolean canSpawnerSpawn(ServerLevel level, BlockPos pos, int range) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(range), e -> e instanceof EntitySano || e instanceof EntityUno).isEmpty();
    }
}
