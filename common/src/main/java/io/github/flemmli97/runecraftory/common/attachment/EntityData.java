package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.LivingEntity;

public class EntityData {

    private boolean sleeping, paralysis, cold, poison;

    public void setSleeping(LivingEntity entity, boolean flag) {
        this.sleeping = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.SLEEP, this.sleeping), entity);
        }
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public void setPoison(LivingEntity entity, boolean flag) {
        this.poison = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.POISON, this.poison), entity);
        }
    }

    public boolean isPoisoned() {
        return this.poison;
    }

    public void setCold(LivingEntity entity, boolean flag) {
        this.cold = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.COLD, this.cold), entity);
        }
    }

    public boolean hasCold() {
        return this.cold;
    }

    public void setParalysis(LivingEntity entity, boolean flag) {
        this.paralysis = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.PARALYSIS, this.paralysis), entity);
        }
    }

    public boolean isParalysed() {
        return this.paralysis;
    }
}