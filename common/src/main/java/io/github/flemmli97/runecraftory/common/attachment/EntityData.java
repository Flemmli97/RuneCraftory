package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.misc.CustomFishingHookEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.mixinhelper.MobToggleHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;

public class EntityData {

    private static final ResourceLocation SLEEP_SOURCE = RuneCraftory.modRes("sleep_ai");

    private final LivingEntity entity;

    private boolean orthoView, enteredBath;

    public CustomFishingHookEntity fishingHook;

    private ItemStack main, off;

    private int invisible;
    private boolean invisibleFlag;

    private final HashSet<Holder<ArmorEffect>> armorFlags = new HashSet<>();

    public EntityData(LivingEntity entity) {
        this.entity = entity;
    }

    public static SleepState getSleepStateFrom(LivingEntity entity) {
        return RunecraftoryAttachments.ENTITY_DATA.get().get(entity).getSleepState();
    }

    public SleepState getSleepState() {
        if (!this.entity.hasEffect(RuneCraftoryEffects.SLEEP.asHolder()))
            return SleepState.NONE;
        if (this.entity instanceof SleepingEntity sleepingEntity && sleepingEntity.hasSleepingAnimation())
            return SleepState.CUSTOM;
        return SleepState.VANILLA;
    }

    public void setSleeping(boolean flag) {
        if (this.entity instanceof MobToggleHandler handler) {
            if (flag) {
                handler.runecraftory$NoAIState().addSource(SLEEP_SOURCE);
            } else {
                handler.runecraftory$NoAIState().removeSource(SLEEP_SOURCE);
            }
        }
        this.setThirdPersonView(flag);
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
        } else {
            ClientHandlers.trySetPerspective(this.entity, flag);
        }
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

    public void tick() {
        if (--this.invisible <= 0 && !this.entity.level().isClientSide()) {
            boolean pre = this.invisibleFlag;
            this.invisibleFlag = this.invisible > 0;
            if (this.invisibleFlag != pre && !this.entity.level().isClientSide) {
                LoaderNetwork.INSTANCE.sendToTracking(new S2CEntityDataSync(this.entity.getId(), S2CEntityDataSync.DataType.INVIS, this.invisibleFlag), this.entity);
            }
        }
    }

    public enum SleepState {
        NONE,
        VANILLA,
        CUSTOM
    }
}
