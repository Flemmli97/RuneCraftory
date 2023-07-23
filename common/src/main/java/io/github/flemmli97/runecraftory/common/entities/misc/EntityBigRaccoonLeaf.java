package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
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

public class EntityBigRaccoonLeaf extends BaseProjectile {

    private static final EntityDataAccessor<Boolean> SPIN = SynchedEntityData.defineId(EntityBigRaccoonLeaf.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> INITIAL_YAW = SynchedEntityData.defineId(EntityBigRaccoonLeaf.class, EntityDataSerializers.FLOAT);

    //Diameter of circle
    private double diameter;
    //The axis to circle around to
    private Vec3 axis;
    //Shooting direction
    private Vec3 dir;
    private Vec3 center;
    private float sumAngles;

    public EntityBigRaccoonLeaf(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public EntityBigRaccoonLeaf(Level world, LivingEntity shooter) {
        super(ModEntities.BIG_RACCOON_LEAF.get(), world, shooter);
        if (shooter.getBbHeight() > 2)
            this.setPos(this.getX(), shooter.getY() + shooter.getBbHeight() * 0.5, this.getZ());
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.entityData.set(INITIAL_YAW, this.getYRot());
        this.dir = this.getDeltaMovement();
        this.axis = this.calculateUpVector(-this.getViewXRot(1), -this.getViewYRot(1));
    }

    public void setDiameter(double diameter) {
        this.diameter = Math.max(diameter, 1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPIN, false);
        this.entityData.define(INITIAL_YAW, 0f);
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.diameter > 0) {
                Vec3 toCenterDir = this.dir.normalize().scale(this.diameter * 0.5);
                if (this.center == null) {
                    this.center = this.position().add(toCenterDir);
                }
                float angle = (float) ((2 * Math.PI / this.livingTickMax()) * this.dir.length());
                if (!this.spinRight())
                    angle *= -1;
                if (this.sumAngles == 0)
                    this.sumAngles += angle * 3;
                double[] point = MathUtils.rotate(this.axis.x, this.axis.y, this.axis.z, -toCenterDir.x, -toCenterDir.y, -toCenterDir.z, this.sumAngles);
                this.sumAngles += angle;
                Vec3 newPos = new Vec3(point[0], point[1], point[2])
                        .add(this.center);
                this.setDeltaMovement(newPos.subtract(this.position()));
                this.hasImpulse = true;
            }
            if (this.sumAngles >= Math.PI * 2)
                this.discard();
        }
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
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(2).element(EnumElement.EARTH), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
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
        this.diameter = compound.getDouble("Diameter");
        ListTag listTag = compound.getList("Axis", Tag.TAG_DOUBLE);
        this.axis = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
        listTag = compound.getList("Direction", Tag.TAG_DOUBLE);
        this.dir = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
        listTag = compound.getList("Center", Tag.TAG_DOUBLE);
        this.center = new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
        listTag = compound.getList("Point", Tag.TAG_DOUBLE);
        this.sumAngles = compound.getFloat("Angles");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("Diameter", this.diameter);
        compound.put("Axis", this.newDoubleList(this.axis.x, this.axis.y, this.axis.z));
        compound.put("Direction", this.newDoubleList(this.dir.x, this.dir.y, this.dir.z));
        compound.put("Center", this.newDoubleList(this.center.x, this.center.y, this.center.z));
        compound.putDouble("Angles", this.sumAngles);
    }
}
