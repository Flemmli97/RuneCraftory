package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityBaseSpellBall extends EntityProjectile {

    private EnumElement element = EnumElement.NONE;
    protected static final EntityDataAccessor<Integer> elementData = SynchedEntityData.defineId(EntityBaseSpellBall.class, EntityDataSerializers.INT);

    public EntityBaseSpellBall(EntityType<? extends EntityBaseSpellBall> type, Level world) {
        super(type, world);
    }

    public EntityBaseSpellBall(Level world, LivingEntity shooter, EnumElement element) {
        super(ModEntities.staffThrown.get(), world, shooter);
        this.element = element;
        this.entityData.set(elementData, this.element.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(elementData, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == elementData) {
            int i = this.entityData.get(elementData);
            if (i < EnumElement.values().length)
                this.element = EnumElement.values()[i];
        }
    }

    @Override
    public int livingTickMax() {
        return 6;
    }

    public EnumElement getElement() {
        return this.element;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).element(this.element).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * 0.8f, null);
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(elementData, compound.getInt("Element"));
        int i = this.entityData.get(elementData);
        if (i < EnumElement.values().length)
            this.element = EnumElement.values()[i];
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
    }
}
