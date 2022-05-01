package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityAmbrosiaWave extends EntityDamageCloud {

    private static final EntityDataAccessor<Integer> maxTick = SynchedEntityData.defineId(EntityAmbrosiaWave.class, EntityDataSerializers.INT);

    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    public static final float circleInc = 0.2f;
    public static final int timeTillFull = 25;

    private static final List<Vector3f> circleParticleMotion = RayTraceUtils.rotatedVecs(new Vec3(0.25, 0, 0), new Vec3(0, 1, 0), -180, 175, 5);

    public EntityAmbrosiaWave(EntityType<? extends EntityAmbrosiaWave> type, Level level) {
        super(type, level);
    }

    public EntityAmbrosiaWave(Level level, LivingEntity shooter, int maxLivingTick) {
        super(ModEntities.ambrosia_wave.get(), level, shooter);
        this.entityData.set(maxTick, maxLivingTick);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> !e.equals(this.getOwner()) && ((BaseMonster) shooter).hitPred.test(e);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(maxTick, 200);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(maxTick, compound.getInt("MaxTick"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MaxTick", this.entityData.get(maxTick));
    }

    @Override
    public float radiusIncrease() {
        return circleInc;
    }

    @Override
    public double maxRadius() {
        return 5;
    }

    @Override
    public boolean canStartDamage() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return this.entityData.get(maxTick);
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        return super.canHit(e) && e.distanceToSqr(this) <= this.getRadius() * this.getRadius() && (this.pred == null || this.pred.test(e));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.livingTicks < this.entityData.get(maxTick) && this.livingTicks % 5 == 1) {
                for (Vector3f vec : circleParticleMotion) {
                    this.level.addParticle(new ColoredParticleData(ModParticles.staticLight.get(), 200 / 255F, 133 / 255F, 36 / 255F, 1, 0.4f), this.getX(), this.getY() + 0.2, this.getZ(), vec.x(), vec.y(), vec.z());
                }
            }
        } else if ((this.getOwner() != null && !this.getOwner().isAlive()))
            this.remove(RemovalReason.KILLED);
    }

    @Override
    protected boolean damageEntity(LivingEntity e) {
        if (CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(4).element(EnumElement.EARTH).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * 0.3f, null)) {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, true, false));
            e.addEffect(new MobEffectInstance(MobEffects.JUMP, 10, 128, true, false));
            return true;
        }
        return false;
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}