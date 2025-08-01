package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FurnitureEntity extends BaseProjectile {

    private static final EntityDataAccessor<Integer> FURNITURE_TYPE_SYNC = SynchedEntityData.defineId(FurnitureEntity.class, EntityDataSerializers.INT);

    private Type furnitureType = Type.CHEST;
    private float randomRotationOffset;

    public FurnitureEntity(EntityType<? extends FurnitureEntity> type, Level level) {
        super(type, level);
        this.randomRotationOffset = this.random.nextFloat() * 360;
        this.setFurnitureType(Type.WOOLYPLUSH);
    }

    public FurnitureEntity(Level level, LivingEntity shooter, Type furnitureType) {
        super(RuneCraftoryEntities.FURNITURE.get(), level, shooter);
        this.setFurnitureType(furnitureType);
    }

    @Override
    public int livingTickMax() {
        return this.isNoGravity() ? 300 : 200;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FURNITURE_TYPE_SYNC, 0);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected float getGravityVelocity() {
        return this.isNoGravity() ? 0 : 0.1f;
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return this.isNoGravity() ? 1 : super.motionReduction(inWater);
    }

    public float getRandomRotationOffset() {
        return this.randomRotationOffset;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        this.refreshDimensions();
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(3).element(ItemElement.DARK), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (att)
            this.discard();
        return att;
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (!this.isAlive()) {
            return null;
        }
        OrientedBoundingBox obb = new OrientedBoundingBox(OrientedBoundingBox.originAABB(this).inflate(0.3)
                .expandTowards(to.subtract(from)),
                0, 0, this.position());
        List<Entity> list = this.level().getEntities(this, obb.getEncompassingBox());
        for (Entity e : list) {
            if (!this.checkedEntities.contains(e.getUUID()) && this.canHit(e) && obb.intersects(e.getBoundingBox())) {
                AABB outer = obb.getEncompassingBox();
                Vec3 hit = new Vec3(Mth.clamp(e.position().x, outer.minX, outer.maxX),
                        Mth.clamp(e.position().y, outer.minY, outer.maxY),
                        Mth.clamp(e.position().z, outer.minZ, outer.maxZ));
                return new EntityHitResult(e, hit);
            }
        }
        return null;
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
            this.refreshDimensions();
            int id = this.entityData.get(FURNITURE_TYPE_SYNC);
            if (id >= 0 && id < Type.values().length)
                this.furnitureType = Type.values()[id];
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(this.furnitureType.range * 2, this.furnitureType.range * 2);
    }

    public Type getFurnitureType() {
        return this.furnitureType;
    }

    protected void setFurnitureType(Type furnitureType) {
        this.furnitureType = furnitureType;
        this.entityData.set(FURNITURE_TYPE_SYNC, this.furnitureType.ordinal());
        this.refreshDimensions();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.furnitureType = FurnitureEntity.Type.valueOf(compound.getString("Type"));
        } catch (IllegalArgumentException e) {
            this.furnitureType = FurnitureEntity.Type.CHEST;
        }
        this.entityData.set(FURNITURE_TYPE_SYNC, this.furnitureType.ordinal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.furnitureType.toString());
    }

    public enum Type {

        CHEST(0.5f),
        BARREL(0.5f),
        ANVIL(0.5f),
        CHAIR(0.4f),
        //PAINTING(1),
        WOOLYPLUSH(0.4f),
        CHIPSQUEEKPLUSH(0.4f);

        final float range;

        Type(float range) {
            this.range = range;
        }
    }
}
