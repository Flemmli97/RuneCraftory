package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

import java.util.List;
import java.util.function.Supplier;

public abstract class EnsembleMonsters extends Entity {

    protected int monsterLevel = 1;
    protected Rotation rotation = Rotation.getRandom(this.random);
    protected int restrictRadius = -1;

    protected EnsembleMonsters(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level instanceof ServerLevel serverLevel) {
            this.spawnEntities(serverLevel);
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void withDirection(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setLevel(int level) {
        this.monsterLevel = level;
    }

    public void setRestrictRadius(int restrictRadius) {
        this.restrictRadius = restrictRadius;
    }

    public abstract List<Supplier<? extends EntityType<?>>> entities();

    public abstract void spawnEntities(ServerLevel serverLevel);

    public abstract boolean canSpawnerSpawn(ServerLevel level, BlockPos pos, int range);
}
