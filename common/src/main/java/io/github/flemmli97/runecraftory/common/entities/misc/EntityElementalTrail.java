package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityElementalTrail extends BaseDamageCloud {

    private static final EntityDataAccessor<Integer> ELEMENT_DATA = SynchedEntityData.defineId(EntityElementalTrail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> STATIONARY = SynchedEntityData.defineId(EntityElementalTrail.class, EntityDataSerializers.BOOLEAN);

    private EnumElement element = EnumElement.NONE;
    private boolean piercing = true;
    private boolean hasKnockback, homing;
    private LivingEntity targetMob;
    private int livingTicksMax = 100;

    public EntityElementalTrail(EntityType<? extends EntityElementalTrail> type, Level level) {
        super(type, level);
    }

    public EntityElementalTrail(Level level, LivingEntity thrower, EnumElement element) {
        super(ModEntities.ELEMENTAL_TRAIL.get(), level, thrower);
        this.setPos(this.getX(), this.getY() + thrower.getBbHeight() * 0.5, this.getZ());
        this.setElement(element);
        this.setRadius(0.5f);
    }

    public void shootAtEntity(Entity target, float velocity, float inaccuracy, float yOffsetModifier, double heighMod) {
        Vec3 dir = (new Vec3(target.getX() - this.getX(), target.getY(heighMod) - this.getY(), target.getZ() - this.getZ()));
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        this.shoot(dir.x, dir.y + l * yOffsetModifier, dir.z, velocity, inaccuracy);
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = Mth.cos(rotationYawIn * 0.017453292F) * Mth.cos(rotationPitchIn * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        Vec3 throwerMotion = entityThrower.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(throwerMotion.x, entityThrower.isOnGround() ? 0.0D : throwerMotion.y, throwerMotion.z));
        this.getDeltaMovement().add(throwerMotion.x, 0, throwerMotion.z);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(EntityProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    protected void setElement(EnumElement element) {
        this.element = element;
        this.entityData.set(ELEMENT_DATA, this.element.ordinal());
    }

    public EnumElement element() {
        return this.element;
    }

    public void setStationary(boolean stationary) {
        this.entityData.set(STATIONARY, stationary);
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
    }

    public void knockback() {
        this.hasKnockback = true;
    }

    public void homing() {
        this.homing = true;
    }

    public void withMaxLiving(int livingTicksMax) {
        this.livingTicksMax = livingTicksMax;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(ELEMENT_DATA)) {
            this.element = EnumElement.values()[this.entityData.get(ELEMENT_DATA)];
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public int livingTickMax() {
        return this.livingTicksMax;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT_DATA, 0);
        this.entityData.define(STATIONARY, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.entityData.get(STATIONARY)) {
            Vec3 motion = this.getDeltaMovement();
            double newX = this.getX() + motion.x;
            double newY = this.getY() + motion.y;
            double newZ = this.getZ() + motion.z;
            this.setPos(newX, newY, newZ);
        }
        if (!this.level.isClientSide) {
            if (this.homing) {
                if (this.targetMob == null || this.targetMob.isDeadOrDying()) {
                    this.targetMob = EntityUtils.ownedProjectileTarget(this.getOwner(), 10);
                } else {
                    Vec3 dir = this.targetMob.position().subtract(this.position());
                    if (dir.lengthSqr() > 0.22 * 0.22)
                        dir = dir.normalize().scale(0.22);
                    this.setDeltaMovement(dir);
                    this.hasImpulse = true;
                }
            }
        } else {
            if (this.livingTicks % 2 == 0) {
                if (this.element == EnumElement.WATER)
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, this.getSoundSource(), 0.9f, 0.8f, false);
                if (this.element == EnumElement.EARTH)
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ROOTED_DIRT_BREAK, this.getSoundSource(), 2, 0.8f, false);
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(this.element), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            if (this.hasKnockback)
                target.knockback(0.5, this.getX() - target.getX(), this.getZ() - target.getZ());
            if (!this.piercing)
                this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.setElement(EnumElement.values()[compound.getInt("Element")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        this.entityData.set(STATIONARY, compound.getBoolean("Stationary"));
        this.piercing = compound.getBoolean("Piercing");
        this.hasKnockback = compound.getBoolean("Knockback");
        this.homing = compound.getBoolean("Homing");
        this.livingTicksMax = compound.getInt("LivingTicksMax");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
        compound.putBoolean("Stationary", this.entityData.get(STATIONARY));
        compound.putBoolean("Piercing", this.piercing);
        compound.putBoolean("Knockback", this.hasKnockback);
        compound.putBoolean("Homing", this.homing);
        compound.putInt("LivingTicksMax", this.livingTicksMax);
    }
}
