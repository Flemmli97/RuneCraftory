package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class EntityHomingEnergyOrb extends BaseDamageCloud implements PowerableMob {

    private Vec3 spawnPos;
    private LivingEntity targetMob;

    protected static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(EntityHomingEnergyOrb.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityHomingEnergyOrb(EntityType<? extends BaseDamageCloud> type, Level world) {
        super(type, world);
        this.setRadius(0.5f);
    }

    public EntityHomingEnergyOrb(Level world, LivingEntity shooter) {
        super(ModEntities.ENERGY_ORB.get(), world, shooter);
        this.setPos(this.getX(), this.getY() + shooter.getBbHeight() * 0.5, this.getZ());
        this.setRadius(0.5f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_UUID, Optional.empty());
    }

    @Override
    public int livingTickMax() {
        return 400;
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).damageType(CustomDamage.DamageType.IGNOREMAGICDEF).noKnockback().hurtResistant(7).element(EnumElement.WIND), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (this.spawnPos == null)
            this.spawnPos = this.position().add(0, this.getBbHeight() * 0.5, 0);
        if (!this.level.isClientSide) {
            if (this.targetMob == null || this.targetMob.isDeadOrDying()) {
                this.targetMob = EntityUtils.ownedProjectileTarget(this.getOwner());
                if (this.targetMob != null)
                    this.entityData.set(TARGET_UUID, Optional.of(this.targetMob.getUUID()));
                else
                    this.entityData.set(TARGET_UUID, Optional.empty());
            } else {
                Vec3 dir = this.targetMob.position().add(0, this.targetMob.getBbHeight() * 0.5, 0).subtract(this.position());
                if (dir.lengthSqr() > 0.27 * 0.27)
                    dir = dir.normalize().scale(0.27);
                this.setDeltaMovement(dir);
                this.hasImpulse = true;
            }
        } else {
            if (this.random.nextBoolean())
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GUARDIAN_ATTACK, this.getSoundSource(), 2, 0.8f, false);
        }
    }

    public LivingEntity getTargetMob() {
        if (this.targetMob != null && !this.targetMob.isRemoved()) {
            return this.targetMob;
        }
        this.entityData.get(TARGET_UUID).ifPresent(uuid -> this.targetMob = EntityUtil.findFromUUID(LivingEntity.class, this.level, uuid));
        return this.targetMob;
    }

    public Vec3 rootPosition(float partialTicks) {
        if (this.getOwner() != null) {
            Entity owner = this.getOwner();
            double x = Mth.lerp(partialTicks, owner.xOld, owner.getX());
            double y = Mth.lerp(partialTicks, owner.yOld, owner.getY()) + owner.getBbHeight() * 0.5;
            double z = Mth.lerp(partialTicks, owner.zOld, owner.getZ());
            return new Vec3(x, y, z);
        }
        return this.spawnPos;
    }

    @Override
    public boolean isPowered() {
        return true;
    }
}
