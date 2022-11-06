package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityButterflySummoner extends Entity implements OwnableEntity {

    @Nullable
    private UUID ownerUUID;
    private LivingEntity caster;
    private double targetX, targetY, targetZ;
    private float damageMultiplier = 0.15f;
    private int ticksExisted;

    public EntityButterflySummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityButterflySummoner(Level level, LivingEntity caster) {
        super(ModEntities.butterflySummoner.get(), level);
        this.caster = caster;
        this.ownerUUID = caster.getUUID();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (!this.level.isClientSide) {
            if (this.getOwner() != null && this.getOwner().isAlive()) {
                EntityButterfly fly = new EntityButterfly(this.level, this.getOwner());
                fly.setDamageMultiplier(this.damageMultiplier);
                fly.setPos(fly.getX() + this.random.nextFloat() * 2 - 1, fly.getY() + this.random.nextFloat() * 0.05 - 0.1, fly.getZ() + this.random.nextFloat() * 2 - 1);
                fly.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.3f, 10);
                this.level.addFreshEntity(fly);
            }
            if (this.ticksExisted >= 40) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

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
        ;
        this.ticksExisted = compound.getInt("TicksExisted");
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
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Entity entity = this.level.getEntity(packet.getData());
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
        } else if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity e = ((ServerLevel) this.level).getEntity(this.ownerUUID);
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
