package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityBaseSpellBall extends BaseProjectile {

    protected static final EntityDataAccessor<Integer> ELEMENT_DATA = SynchedEntityData.defineId(EntityBaseSpellBall.class, EntityDataSerializers.INT);

    private EnumElement element = EnumElement.NONE;

    public EntityBaseSpellBall(EntityType<? extends EntityBaseSpellBall> type, Level world) {
        super(type, world);
    }

    public EntityBaseSpellBall(Level world, LivingEntity shooter, EnumElement element) {
        super(ModEntities.STAFF_BASE_PROJECTILE.get(), world, shooter);
        this.element = element;
        this.entityData.set(ELEMENT_DATA, this.element.ordinal());
        this.damageMultiplier = 0.8f;
    }

    public EnumElement getElement() {
        return this.element;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == ELEMENT_DATA) {
            int i = this.entityData.get(ELEMENT_DATA);
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
        this.entityData.define(ELEMENT_DATA, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().element(this.element).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ELEMENT_DATA, compound.getInt("Element"));
        int i = this.entityData.get(ELEMENT_DATA);
        if (i < EnumElement.values().length)
            this.element = EnumElement.values()[i];
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
    }
}
