package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityFurniture extends BaseProjectile {

    private static final EntityDataAccessor<Integer> FURNITURE_TYPE_SYNC = SynchedEntityData.defineId(EntityFurniture.class, EntityDataSerializers.INT);

    private Type furnitureType = Type.BARREL;

    public EntityFurniture(EntityType<? extends EntityFurniture> type, Level world) {
        super(type, world);
    }

    public EntityFurniture(Level world, LivingEntity shooter, Type furnitureType) {
        super(ModEntities.FURNITURE.get(), world, shooter);
        this.setFurnitureType(furnitureType);
    }

    @Override
    public int livingTickMax() {
        return this.isNoGravity() ? 400 : 200;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FURNITURE_TYPE_SYNC, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1f;
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return this.isNoGravity() ? 1 : super.motionReduction(inWater);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(1).element(EnumElement.DARK), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (!this.isAlive()) {
            return null;
        } else {
            return this.entityCollision(from, to, this::canHit);
        }
    }

    private EntityHitResult entityCollision(Vec3 from, Vec3 to, Predicate<Entity> pred) {
        double distVar = Double.MAX_VALUE;
        Entity ret = null;
        AABB entityBB = this.getBoundingBox();
        for (Entity entity1 : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()), pred)) {
            AABB axisalignedbb = entity1.getBoundingBox().inflate(0.3F);
            if (entityBB.intersects(axisalignedbb)) {
                double dist = this.position().distanceToSqr(entity1.position());
                if (dist < distVar) {
                    ret = entity1;
                    distVar = dist;
                }
            }
        }
        return ret == null ? null : new EntityHitResult(ret);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.isNoGravity() && blockHitResult.getDirection() == Direction.UP)
            this.discard();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == FURNITURE_TYPE_SYNC) {
            int id = this.entityData.get(FURNITURE_TYPE_SYNC);
            if (id >= 0 && id < Type.values().length)
                this.furnitureType = Type.values()[id];
        }
    }

    public Type getFurnitureType() {
        return this.furnitureType;
    }

    protected void setFurnitureType(Type furnitureType) {
        this.furnitureType = furnitureType;
        this.entityData.set(FURNITURE_TYPE_SYNC, this.furnitureType.ordinal());
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
