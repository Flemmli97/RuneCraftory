package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntitySpike extends BaseDamageCloud {

    private static final int ATTACK_DURATION = 20;
    private static final int ATTACK_TICK = 9;

    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntitySpike.class, EntityDataSerializers.INT);

    private int targetDuration, attackDelay;
    private int attackTime, clientAttackTime;
    private LivingEntity targetMob;
    private boolean attacking;

    public int clientLightLevelHeight;

    public EntitySpike(EntityType<? extends EntitySpike> entityType, Level level) {
        super(entityType, level);
    }

    public EntitySpike(Level level, LivingEntity livingEntity, int targetDuration, int attackDelay, SpikeType spikeType) {
        super(ModEntities.HOMING_SPIKES.get(), level, livingEntity);
        this.targetDuration = targetDuration;
        this.attackDelay = attackDelay;
        this.entityData.set(TYPE, spikeType.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, 0);
    }

    public void setEntityTarget(LivingEntity target) {
        this.targetMob = target;
    }

    @Override
    public Vec3 getLightProbePosition(float partialTicks) {
        return this.getPosition(partialTicks).add(0, 2.1, 0);
    }

    @Override
    public int livingTickMax() {
        return this.targetDuration + this.attackDelay + ATTACK_DURATION;
    }

    public int spikeType() {
        return this.entityData.get(TYPE);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.attackDelay = compound.getInt("AttackDelay");
        this.targetDuration = compound.getInt("TargetDuration");
        this.attackTime = compound.getInt("AttackTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackDelay", this.attackDelay);
        compound.putInt("TargetDuration", this.targetDuration);
        compound.putInt("AttackTime", this.attackTime);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (this.level.isClientSide) {
            if (!this.attacking) {
                double y = this.getY();
                BlockState current = this.level.getBlockState(this.blockPosition());
                if (current.isAir()) {
                    current = this.level.getBlockState(this.blockPosition().below());
                    if (current.isAir()) {
                        current = Blocks.DIRT.defaultBlockState();
                    } else {
                        y = Mth.floor(y);
                    }
                } else {
                    y = Mth.ceil(y);
                }
                for (int i = 0; i < 6; ++i) {
                    double dX = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                    double dY = 0.3 + this.random.nextDouble() * 0.3;
                    double dZ = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                    double pY = y + this.random.nextDouble() * 0.5 - 0.25;
                    this.clientLightLevelHeight = Mth.floor(y);
                    this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, current),
                            this.getRandomX(2), pY, this.getRandomZ(2),
                            dX, dY, dZ);
                }
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), current.getSoundType().getStepSound(), this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.1f + 0.75f, false);
            }
        } else {
            if (this.livingTicks < this.targetDuration) {
                if (this.targetMob == null || this.targetMob.isDeadOrDying()) {
                    this.targetMob = EntityUtils.ownedProjectileTarget(this.getOwner(), 12);
                }
                if (this.targetMob != null) {
                    Vec3 dir = this.targetMob.position().subtract(this.position()).normalize().scale(0.2);
                    if (this.damageBoundingBox().inflate(-0.25).intersects(this.targetMob.getBoundingBox())) {
                        dir = Vec3.ZERO;
                        this.livingTicks = this.targetDuration;
                    }
                    this.setDeltaMovement(dir);
                    this.hasImpulse = true;
                }
            } else if (++this.attackTime > this.attackDelay) {
                this.setDeltaMovement(Vec3.ZERO);
                if (!this.attacking) {
                    this.level.broadcastEntityEvent(this, (byte) 4);
                    this.attacking = true;
                }
            }
        }
    }

    @Override
    public boolean canStartDamage() {
        return this.attackTime == ATTACK_TICK;
    }

    @Override
    protected AABB damageBoundingBox() {
        return new AABB(-0.6, -0.2, -0.6, 0.6, 2.2, 0.6).move(this.position());
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.EARTH).magic().hurtResistant(0);
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, builder, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            this.attacking = true;
            this.clientAttackTime = this.livingTicks;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EAT, this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.1f + 0.75f, false);
            }
        }
    }

    public float getAnimationProgress(float partialTicks) {
        if (!this.attacking) {
            return 0.0f;
        }
        int i = this.livingTicks - this.clientAttackTime;
        float lerped = Math.min(ATTACK_DURATION, Mth.lerp(partialTicks, i, i + 1));
        return Mth.sin(lerped * Mth.PI / ATTACK_DURATION) * 1.2f;
    }

    public enum SpikeType {
        EARTH,
        ROOT
    }
}

