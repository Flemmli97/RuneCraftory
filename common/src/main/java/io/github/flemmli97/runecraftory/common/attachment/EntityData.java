package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EntityData {

    private boolean sleeping, paralysis, cold, poison, invis, orthoView;

    public EntityCustomFishingHook fishingHook;

    private ItemStack main, off;

    public void setSleeping(LivingEntity entity, boolean flag) {
        this.sleeping = flag;
        this.setOrthoView(entity, flag);
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.SLEEP, this.sleeping), entity);
        } else
            ClientHandlers.grabMouse(entity);
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

    public void setInvis(LivingEntity entity, boolean flag) {
        this.invis = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.INVIS, this.invis), entity);
        }
    }

    public boolean isInvis() {
        return this.invis;
    }

    public void setOrthoView(LivingEntity entity, boolean flag) {
        this.orthoView = flag;
        if (!entity.level.isClientSide) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.ORTHOVIEW, this.orthoView), entity);
        } else
            ClientHandlers.trySetPerspective(entity, flag);
    }

    public boolean isOrthoView() {
        return this.orthoView;
    }

    public ItemStack getGloveOffHand(ItemStack stack) {
        if (stack != null && this.main != stack) {
            this.main = stack;
            this.off = this.main.copy();
        }
        return this.off;
    }
}
