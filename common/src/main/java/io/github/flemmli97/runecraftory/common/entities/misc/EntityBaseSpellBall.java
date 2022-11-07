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

    protected static final EntityDataAccessor<Integer> elementData = SynchedEntityData.defineId(EntityBaseSpellBall.class, EntityDataSerializers.INT);
    private EnumElement element = EnumElement.NONE;
    private float damageMultiplier = 0.8f;

    public EntityBaseSpellBall(EntityType<? extends EntityBaseSpellBall> type, Level world) {
        super(type, world);
    }

    public EntityBaseSpellBall(Level world, LivingEntity shooter, EnumElement element) {
        super(ModEntities.staffThrown.get(), world, shooter);
        this.element = element;
        this.entityData.set(elementData, this.element.ordinal());
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public EnumElement getElement() {
        return this.element;
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(elementData, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).element(this.element).hurtResistant(5), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(elementData, compound.getInt("Element"));
        int i = this.entityData.get(elementData);
        if (i < EnumElement.values().length)
            this.element = EnumElement.values()[i];
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
