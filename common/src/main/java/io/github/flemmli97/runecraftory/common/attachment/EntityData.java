package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;

public class EntityData {

    private boolean sleeping, isSilent, paralysis, stunned, noAIStunned, cold, poison, invis, orthoView, enteredBath;
    private int disabledState;

    public EntityCustomFishingHook fishingHook;

    private ItemStack main, off;

    public float sleepYRot;

    private final HashSet<Holder<ArmorEffect>> armorFlags = new HashSet<>();

    public static SleepState getSleepStateFrom(LivingEntity entity) {
        return Platform.INSTANCE.getEntityData(entity).getSleepState(entity);
    }

    public SleepState getSleepState(LivingEntity entity) {
        if (!this.isSleeping())
            return SleepState.NONE;
        if (entity instanceof SleepingEntity sleeping && sleeping.hasSleepingAnimation())
            return SleepState.CUSTOM;
        return SleepState.VANILLA;
    }

    public void setSleeping(LivingEntity entity, boolean flag) {
        this.sleeping = flag;
        this.updateAiState(entity, flag);
        this.setOrthoView(entity, flag);
        this.sleepYRot = entity.yBodyRot;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.SLEEP, this.sleeping), entity);
        } else
            ClientHandlers.grabMouse(entity, this.sleeping);
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public void setPoison(LivingEntity entity, boolean flag) {
        this.poison = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.POISON, this.poison), entity);
        }
    }

    public boolean isPoisoned() {
        return this.poison;
    }

    public void setCold(LivingEntity entity, boolean flag) {
        this.cold = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.COLD, this.cold), entity);
        }
    }

    public boolean hasCold() {
        return this.cold;
    }

    public void setParalysis(LivingEntity entity, boolean flag) {
        this.paralysis = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.PARALYSIS, this.paralysis), entity);
        }
    }

    public boolean isParalysed() {
        return this.paralysis;
    }

    public void setStunned(LivingEntity entity, boolean flag) {
        this.stunned = flag;
        this.updateAiState(entity, flag);
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.STUN, this.stunned), entity);
        }
    }

    public boolean isStunned() {
        return this.stunned;
    }

    public void setInvis(LivingEntity entity, boolean flag) {
        this.invis = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invis), entity);
        }
    }

    public boolean isInvis() {
        return this.invis;
    }

    public void setOrthoView(LivingEntity entity, boolean flag) {
        this.orthoView = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.ORTHOVIEW, this.orthoView), entity);
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

    public void addArmorFlag(Holder<ArmorEffect> key) {
        this.armorFlags.add(key);
    }

    public void removeArmorFlag(Holder<ArmorEffect> key) {
        this.armorFlags.remove(key);
    }

    public boolean hasArmorFlag(Holder<ArmorEffect> key) {
        return this.armorFlags.contains(key);
    }

    public boolean enteredBath() {
        return this.enteredBath;
    }

    public EntityData setEnteredBath(boolean enteredBath) {
        this.enteredBath = enteredBath;
        return this;
    }

    private void updateAiState(LivingEntity entity, boolean increase) {
        int pre = this.disabledState;
        if (increase)
            this.disabledState++;
        else
            this.disabledState--;
        if (pre == 0 && increase) {
            if (entity instanceof Mob mob) {
                this.noAIStunned = mob.isNoAi();
                if (!this.noAIStunned)
                    mob.setNoAi(true);
            }
            this.isSilent = entity.isSilent();
            if (!this.isSilent)
                entity.setSilent(true);
        } else if (pre == 1 && !increase) {
            if (entity instanceof Mob mob && !this.noAIStunned) {
                mob.setNoAi(false);
            }
            if (!this.isSilent) {
                entity.setSilent(false);
            }
        }
    }

    public enum SleepState {
        NONE,
        VANILLA,
        CUSTOM
    }
}
