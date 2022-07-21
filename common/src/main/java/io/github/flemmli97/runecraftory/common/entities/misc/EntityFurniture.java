package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntityFurniture extends EntityProjectile {

    private static final EntityDataAccessor<Integer> furnitureTypeSync = SynchedEntityData.defineId(EntityFurniture.class, EntityDataSerializers.INT);

    private Type furnitureType = Type.BARREL;
    private float damageMultiplier;

    private Predicate<LivingEntity> pred;

    public EntityFurniture(EntityType<? extends EntityProjectile> type, Level world) {
        super(type, world);
    }

    public EntityFurniture(Level world, LivingEntity shooter, Type furnitureType) {
        super(ModEntities.furniture.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
        this.setFurnitureType(furnitureType);
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int livingTickMax() {
        return 800;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(furnitureTypeSync, 0);
    }

    @Override
    protected boolean canHit(Entity e) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.canHit(e);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1f;
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return this.isNoGravity() ? 0 : super.motionReduction(inWater);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.isNoGravity() && blockHitResult.getDirection() == Direction.UP)
            this.remove(RemovalReason.KILLED);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == furnitureTypeSync) {
            int id = this.entityData.get(furnitureTypeSync);
            if (id > 0 && id < Type.values().length)
                this.furnitureType = Type.values()[this.entityData.get(furnitureTypeSync)];
        }
    }

    public Type getFurnitureType() {
        return this.furnitureType;
    }

    protected void setFurnitureType(Type furnitureType) {
        this.furnitureType = furnitureType;
        this.entityData.set(furnitureTypeSync, this.furnitureType.ordinal());
    }

    public enum Type {

        CHEST(0.5f),
        BARREL(0.5f),
        ANVIL(0.5f),
        CHAIR(0.4f),
        PAINTING(1),
        WOOLYPLUSH(0.4f),
        CHIPSQUEEKPLUSH(0.4f);

        final float range;

        Type(float range) {
            this.range = range;
        }
    }
}
