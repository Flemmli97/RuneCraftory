package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityWispFlame extends EntityDamageCloud {

    private static final EntityDataAccessor<Integer> elementData = SynchedEntityData.defineId(EntityWispFlame.class, EntityDataSerializers.INT);

    private EnumElement element = EnumElement.NONE;
    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntityWispFlame(EntityType<? extends EntityWispFlame> type, Level level) {
        super(type, level);
    }

    public EntityWispFlame(Level level, double x, double y, double z) {
        super(ModEntities.wispFlame.get(), level, x, y, z);
        this.setRadius(1.5f);
    }

    public EntityWispFlame(Level level, LivingEntity thrower, EnumElement element) {
        super(ModEntities.wispFlame.get(), level, thrower);
        this.setPos(this.getX(), this.getY() + thrower.getBbHeight() * 0.5, this.getZ());
        this.setElement(element);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).hitPred;
        this.setRadius(1.5f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(elementData)) {
            this.element = EnumElement.values()[this.entityData.get(elementData)];
        }
        super.onSyncedDataUpdated(key);
    }

    protected void setElement(EnumElement element) {
        this.element = element;
        this.entityData.set(elementData, this.element.ordinal());
    }

    public EnumElement element() {
        return this.element;
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(elementData, 0);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
    }

    @Override
    protected boolean canHit(LivingEntity entity) {
        return super.canHit(entity) && (this.pred == null || this.pred.test(entity));
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        if (CombatUtils.damage(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).element(this.element).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            target.knockback(0.5, this.getX() - target.getX(), this.getZ() - target.getZ());
            return true;
        }
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        try {
            this.setElement(EnumElement.valueOf(compound.getString("Element")));
        } catch (IllegalArgumentException ignored) {
        }
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.element.toString());
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
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

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
}
