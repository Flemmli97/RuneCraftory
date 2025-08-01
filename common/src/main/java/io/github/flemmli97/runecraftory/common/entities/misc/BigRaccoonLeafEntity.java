package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class BigRaccoonLeafEntity extends BaseProjectile {

    private static final EntityDataAccessor<Boolean> SPIN = SynchedEntityData.defineId(BigRaccoonLeafEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> INITIAL_YAW = SynchedEntityData.defineId(BigRaccoonLeafEntity.class, EntityDataSerializers.FLOAT);

    private Vec3 shootDir, center, axis;
    private float circleRadius;

    public BigRaccoonLeafEntity(EntityType<? extends BaseProjectile> type, Level level) {
        super(type, level);
    }

    public BigRaccoonLeafEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.BIG_RACCOON_LEAF.get(), level, shooter);
        if (shooter.getBbHeight() > 2)
            this.setPos(this.getX(), shooter.getY() + shooter.getBbHeight() * 0.5, this.getZ());
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.entityData.set(INITIAL_YAW, this.getYRot());
        this.shootDir = this.getDeltaMovement();
        this.center = this.position().add(this.shootDir);
        this.axis = MathsHelper.getUp(this.getDeltaMovement());
    }

    public void setCenter(float radius) {
        Vec3 dir = this.getShootDir().normalize().scale(radius);
        this.center = this.position().add(dir);
        this.axis = MathsHelper.getUp(dir);
        this.circleRadius = radius;
    }

    private Vec3 getShootDir() {
        return this.shootDir == null ? this.getDeltaMovement() : this.shootDir;
    }

    @Override
    public float radius() {
        return 0.5f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPIN, false);
        builder.define(INITIAL_YAW, 0f);
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            Vec3 dir = this.position().subtract(this.center)
                    .normalize().scale(this.circleRadius);
            float angle = (float) (2 * Math.PI / this.livingTickMax());
            if (!this.spinRight())
                angle *= -1;
            if (this.firstTick)
                angle *= 2;
            Vector3d point = new Vector3d(dir.x, dir.y, dir.z)
                    .rotateAxis(angle, this.axis.x(), this.axis.y(), this.axis.z());
            Vec3 newPos = this.center.add(point.x(), point.y(), point.z());
            this.setDeltaMovement(newPos.subtract(this.position()));
            this.hasImpulse = true;
        }
        super.tick();
    }

    public void withRightSpin(boolean spin) {
        this.entityData.set(SPIN, spin);
    }

    public boolean spinRight() {
        return this.entityData.get(SPIN);
    }

    public float initialYaw() {
        return this.entityData.get(INITIAL_YAW);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(2).element(ItemElement.EARTH), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (att)
            this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ListTag listTag = compound.getList("Axis", Tag.TAG_DOUBLE);
        this.axis = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
        listTag = compound.getList("Center", Tag.TAG_DOUBLE);
        this.center = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Axis", this.newDoubleList(this.axis.x, this.axis.y, this.axis.z));
        compound.put("Center", this.newDoubleList(this.center.x, this.center.y, this.center.z));
    }
}
