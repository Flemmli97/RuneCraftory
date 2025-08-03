package io.github.flemmli97.runecraftory.common.entities.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class ProjectileSummonHelperEntity extends Entity implements OwnableEntity {

    @Nullable
    private UUID ownerUUID;
    private LivingEntity caster;
    protected double targetX, targetY, targetZ;
    protected float damageMultiplier = 1;
    protected int ticksExisted;
    protected int maxLivingTicks = 40;

    protected Vec3 lockedPosition;
    protected float lockedYaw, lockedPitch;

    public ProjectileSummonHelperEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSummonHelperEntity(EntityType<?> entityType, Level level, LivingEntity caster) {
        super(entityType, level);
        this.setPos(caster.getX(), caster.getY() + caster.getEyeHeight() - 0.1D, caster.getZ());
        this.caster = caster;
        this.ownerUUID = caster.getUUID();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    public int getMaxLivingTicks() {
        return this.maxLivingTicks;
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (this.lockedPosition == null) {
            this.lockedPosition = this.position();
            this.lockedPitch = this.getXRot();
            this.lockedYaw = this.getYRot();
        }
        this.setPos(this.lockedPosition);
        this.setXRot(this.lockedPitch);
        this.setYRot(this.lockedYaw);
        if (!this.level().isClientSide) {
            if (this.getOwner() != null && this.getOwner().isAlive()) {
                this.summonProjectiles();
            }
            if (this.ticksExisted >= this.getMaxLivingTicks()) {
                this.discard();
            }
        }
    }

    protected abstract void summonProjectiles();

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.caster = this.getOwner();
        this.targetX = compound.getDouble("TargetX");
        this.targetY = compound.getDouble("TargetY");
        this.targetZ = compound.getDouble("TargetZ");
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
        this.ticksExisted = compound.getInt("TicksExisted");
        this.maxLivingTicks = compound.getInt("MaxLivingTicks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putDouble("TargetX", this.targetX);
        compound.putDouble("TargetY", this.targetY);
        compound.putDouble("TargetZ", this.targetZ);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
        compound.putInt("TicksExisted", this.ticksExisted);
        compound.putInt("MaxLivingTicks", this.maxLivingTicks);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        Entity owner = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity, owner == null ? 0 : owner.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Entity entity = this.level().getEntity(packet.getData());
        if (entity instanceof LivingEntity livingEntity) {
            this.setOwner(livingEntity);
        }
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public void setTarget(double x, double y, double z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        double dX = x - this.getX();
        double dY = y - this.getY();
        double dZ = z - this.getZ();
        this.setYRot((float) (Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 90);
        this.setXRot((float) -(Mth.atan2(dY, Math.sqrt(dX * dX + dZ * dZ)) * Mth.RAD_TO_DEG));
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.caster != null && !this.caster.isRemoved()) {
            return this.caster;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity e = serverLevel.getEntity(this.ownerUUID);
            if (e instanceof LivingEntity livingEntity) {
                this.caster = livingEntity;
            }
            return this.caster;
        } else {
            return null;
        }
    }

    public void setOwner(@Nullable LivingEntity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            this.caster = entity;
        }
    }
}
