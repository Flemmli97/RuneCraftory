package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.misc.CustomFishingHookEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;

public class EntityData {

    public static final ResourceLocation EARTH_DEBUFF = RuneCraftory.modRes("earth_debuff");

    private final LivingEntity entity;

    private boolean sleeping, isSilent, paralysis, stunned, noAIStunned, cold, poison, orthoView, enteredBath;
    private int disabledState;

    public CustomFishingHookEntity fishingHook;

    private ItemStack main, off;

    public float sleepYRot;

    private int invisible;
    private boolean invisibleFlag;

    private int earthDebuff;

    private final HashSet<Holder<ArmorEffect>> armorFlags = new HashSet<>();

    public EntityData(LivingEntity entity) {
        this.entity = entity;
    }

    public static SleepState getSleepStateFrom(LivingEntity entity) {
        return Platform.INSTANCE.getEntityData(entity).getSleepState();
    }

    public SleepState getSleepState() {
        if (!this.isSleeping())
            return SleepState.NONE;
        if (this.entity instanceof SleepingEntity sleepingEntity && sleepingEntity.hasSleepingAnimation())
            return SleepState.CUSTOM;
        return SleepState.VANILLA;
    }

    public void setSleeping(boolean flag) {
        this.sleeping = flag;
        this.updateAiState(flag);
        this.setThirdPersonView(flag);
        this.sleepYRot = this.entity.yBodyRot;
        if (!this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.SLEEP, this.sleeping), this.entity);
        } else
            ClientHandlers.grabMouse(this.entity, this.sleeping);
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

    public void setCold(boolean flag) {
        this.cold = flag;
        if (!this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.COLD, this.cold), this.entity);
        }
    }

    public boolean hasCold() {
        return this.cold;
    }

    public void setParalysis(boolean flag) {
        this.paralysis = flag;
        if (!this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.PARALYSIS, this.paralysis), this.entity);
        }
    }

    public boolean isParalysed() {
        return this.paralysis;
    }

    public void setStunned(boolean flag) {
        this.stunned = flag;
        this.updateAiState(flag);
        if (!this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.STUN, this.stunned), this.entity);
        }
    }

    public boolean isStunned() {
        return this.stunned;
    }

    public void setInvis(int duration) {
        this.invisible = duration;
        boolean pre = this.invisibleFlag;
        this.invisibleFlag = duration > 0;
        if (this.invisibleFlag != pre && !this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invisibleFlag), this.entity);
        }
    }

    public boolean isInvisible() {
        return this.invisibleFlag;
    }

    public void setThirdPersonView(boolean flag) {
        this.orthoView = flag;
        if (!this.entity.level().isClientSide) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.ORTHOVIEW, this.orthoView), this.entity);
        } else
            ClientHandlers.trySetPerspective(this.entity, flag);
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

    public void applyEarthDebuff() {
        if (!this.entity.level().isClientSide()) {
            AttributeInstance inst = this.entity.getAttribute(Attributes.ARMOR);
            if (inst != null && !inst.hasModifier(EARTH_DEBUFF)) {
                inst.addTransientModifier(new AttributeModifier(EARTH_DEBUFF, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            this.earthDebuff = 200;
        }
    }

    private void updateAiState(boolean increase) {
        int pre = this.disabledState;
        if (increase)
            this.disabledState++;
        else
            this.disabledState--;
        if (pre == 0 && increase) {
            if (this.entity instanceof Mob mob) {
                this.noAIStunned = mob.isNoAi();
                if (!this.noAIStunned)
                    mob.setNoAi(true);
            }
            this.isSilent = this.entity.isSilent();
            if (!this.isSilent)
                this.entity.setSilent(true);
        } else if (pre == 1 && !increase) {
            if (this.entity instanceof Mob mob && !this.noAIStunned) {
                mob.setNoAi(false);
            }
            if (!this.isSilent) {
                this.entity.setSilent(false);
            }
        }
    }

    public void tick() {
        if (--this.invisible <= 0 && !this.entity.level().isClientSide()) {
            boolean pre = this.invisibleFlag;
            this.invisibleFlag = this.invisible > 0;
            if (this.invisibleFlag != pre && !this.entity.level().isClientSide) {
                LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invisibleFlag), this.entity);
            }
        }
        if (!this.entity.level().isClientSide()) {
            if (--this.earthDebuff == 0) {
                AttributeInstance inst = this.entity.getAttribute(Attributes.ARMOR);
                if (inst != null && inst.hasModifier(EARTH_DEBUFF)) {
                    inst.removeModifier(EARTH_DEBUFF);
                }
            }
        }
    }

    public enum SleepState {
        NONE,
        VANILLA,
        CUSTOM
    }
}
