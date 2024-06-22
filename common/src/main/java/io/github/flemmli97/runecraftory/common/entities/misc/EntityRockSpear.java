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
import net.minecraft.world.phys.EntityHitResult;

public class EntityRockSpear extends BaseBeam {

    private static final EntityDataAccessor<Boolean> BIG = SynchedEntityData.defineId(EntityRockSpear.class, EntityDataSerializers.BOOLEAN);

    public EntityRockSpear(EntityType<? extends EntityRockSpear> type, Level world) {
        super(type, world);
    }

    public EntityRockSpear(Level world, LivingEntity shooter, boolean big) {
        super(ModEntities.ROCK_SPEAR.get(), world, shooter);
        this.setPos(this.getX(), this.getY() - 0.1, this.getZ());
        this.entityData.set(BIG, big);
    }

    public boolean bigRock() {
        return this.entityData.get(BIG);
    }

    @Override
    public float getRange() {
        return this.bigRock() ? 8 : 4;
    }

    @Override
    public float radius() {
        return this.bigRock() ? 0.8f : 0.5f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BIG, false);
    }

    @Override
    public void onImpact(EntityHitResult entityHitResult) {
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), entityHitResult.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().element(EnumElement.EARTH).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public boolean canStartDamage() {
        return (this.livingTicks - 1) == 5;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(BIG, compound.getBoolean("BigRock"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("BigRock", this.bigRock());
    }
}
