package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityAmbrosiaWave extends BaseDamageCloud {

    private static final EntityDataAccessor<Integer> MAX_TICK = SynchedEntityData.defineId(EntityAmbrosiaWave.class, EntityDataSerializers.INT);
    private static final List<Vector3f> CIRCLE_PARTICLE_MOTION = RayTraceUtils.rotatedVecs(new Vec3(0.25, 0, 0), new Vec3(0, 1, 0), -180, 175, 5);

    private final Set<FrozenEntity> hitEntityPos = new HashSet<>();

    public EntityAmbrosiaWave(EntityType<? extends EntityAmbrosiaWave> type, Level level) {
        super(type, level);
        this.damageMultiplier = 0.3f;
    }

    public EntityAmbrosiaWave(Level level, LivingEntity shooter, int maxLivingTick) {
        super(ModEntities.AMBROSIA_WAVE.get(), level, shooter);
        this.entityData.set(MAX_TICK, maxLivingTick);
        this.damageMultiplier = 0.3f;
    }

    @Override
    public float radiusIncrease() {
        return 0.5f;
    }

    @Override
    public double maxRadius() {
        return 5;
    }

    @Override
    public int livingTickMax() {
        return this.entityData.get(MAX_TICK);
    }

    @Override
    public boolean canStartDamage() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAX_TICK, 200);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.livingTicks < (this.entityData.get(MAX_TICK) - 8) && this.livingTicks % 5 == 1) {
                for (Vector3f vec : CIRCLE_PARTICLE_MOTION) {
                    this.level.addParticle(new ColoredParticleData(ModParticles.STATIC_LIGHT.get(), 200 / 255F, 133 / 255F, 36 / 255F, 1, 0.4f), this.getX(), this.getY() + 0.2, this.getZ(), vec.x(), vec.y(), vec.z());
                }
            }
        } else {
            if (this.canStartDamage())
                this.playSound(ModSounds.ENTITY_AMBROSIA_WAVE.get(), 1, 1);
            if ((this.getOwner() != null && !this.getOwner().isAlive()))
                this.discard();
            this.hitEntityPos.forEach(frozenEntity -> {
                if (frozenEntity.entity instanceof ServerPlayer player)
                    player.moveTo(frozenEntity.pos.x(), frozenEntity.pos.y(), frozenEntity.pos.z());
                else
                    frozenEntity.entity.setPos(frozenEntity.pos.x(), frozenEntity.pos.y(), frozenEntity.pos.z());
            });
        }
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        return super.canHit(e) && e.distanceToSqr(this) <= this.getRadius() * this.getRadius();
    }

    @Override
    protected AABB damageBoundingBox() {
        float radius = this.getRadius();
        return this.getBoundingBox().inflate(radius, 0.75, radius);
    }

    @Override
    protected boolean damageEntity(LivingEntity e) {
        CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(4).element(EnumElement.EARTH)
                .withChangedAttribute(ModAttributes.DRAIN.get(), 50);
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, builder, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, true, false));
            this.hitEntityPos.add(new FrozenEntity(e, e.position()));
            return true;
        }
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(MAX_TICK, compound.getInt("MaxTick"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MaxTick", this.entityData.get(MAX_TICK));
    }

    record FrozenEntity(LivingEntity entity, Vec3 pos) {

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FrozenEntity frozenEntity)
                return this.entity == frozenEntity.entity;
            return false;
        }

        @Override
        public int hashCode() {
            return this.entity.getUUID().hashCode();
        }
    }
}
