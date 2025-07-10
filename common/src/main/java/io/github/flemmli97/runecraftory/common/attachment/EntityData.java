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

    private boolean sleeping, isSilent, paralysis, stunned, noAIStunned, cold, poison, orthoView, enteredBath;
    private int disabledState;

    public EntityCustomFishingHook fishingHook;

    private ItemStack main, off;

    public float sleepYRot;

    private int invisible;
    private boolean invisibleFlag;

    private final HashSet<Holder<ArmorEffect>> armorFlags = new HashSet<>();

    public static SleepState getSleepStateFrom(LivingEntity entity) {
        return Platform.INSTANCE.getEntityData(entity).getSleepState(entity);
    }

    public SleepState getSleepState(LivingEntity entity) {
        if (!this.isSleeping())
            return SleepState.NONE;
        if (entity instanceof SleepingEntity sleepingEntity && sleepingEntity.hasSleepingAnimation())
            return SleepState.CUSTOM;
        return SleepState.VANILLA;
    }

    public void setSleeping(LivingEntity entity, boolean flag) {
        this.sleeping = flag;
        this.updateAiState(entity, flag);
        this.setThirdPersonView(entity, flag);
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

    public void setInvis(LivingEntity entity, int duration) {
        this.invisible = duration;
        boolean pre = this.invisibleFlag;
        this.invisibleFlag = duration > 0;
        if (this.invisibleFlag != pre && !entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invisibleFlag), entity);
        }
    }

    public boolean isInvisible() {
        return this.invisibleFlag;
    }

    public void setThirdPersonView(LivingEntity entity, boolean flag) {
        this.orthoView = flag;
        if (!entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.ORTHOVIEW, this.orthoView), entity);
        } else
            ClientHandlers.trySetPerspective(entity, flag);
    }

    public boolean thirdPersonView() {
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

    public void setEnteredBath(boolean enteredBath) {
        this.enteredBath = enteredBath;
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

    public void tick(LivingEntity entity) {
        if (--this.invisible <= 0 && !entity.level().isClientSide()) {
            boolean pre = this.invisibleFlag;
            this.invisibleFlag = this.invisible > 0;
            if (this.invisibleFlag != pre && !entity.level().isClientSide) {
                LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invisibleFlag), entity);
            }
        }
    }

    public enum SleepState {
        NONE,
        VANILLA,
        CUSTOM
    }
}
