package io.github.flemmli97.runecraftory.common.entities.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityAggressiveWooly extends EntityWooly {

    public EntityAggressiveWooly(EntityType<? extends EntityWooly> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        this.entityData.set(SPAWNSHEARED, false);
        this.setSheared(false);
    }

    @Override
    protected float attackChance() {
        return 1;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.9f;
    }
}
