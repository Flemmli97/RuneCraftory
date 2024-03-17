package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySlashResidue extends BaseDamageCloud {

    private static final Vec3[] BASE = new Vec3[]{
            Vec3.ZERO,
            new Vec3(0.5f, 0, 0),
            new Vec3(-0.5f, 0, 0)
    };

    protected static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(EntitySlashResidue.class, EntityDataSerializers.FLOAT);

    private boolean oneTimeDamage;

    public EntitySlashResidue(EntityType<? extends BaseDamageCloud> type, Level world) {
        super(type, world);
    }

    public EntitySlashResidue(Level world, LivingEntity shooter) {
        super(ModEntities.SLASH_RESIDUE.get(), world, shooter);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 1f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIZE.equals(key)) {
            this.refreshDimensions();
        }
    }

    public void setSize(float size) {
        this.entityData.set(SIZE, Math.max(0.2f, size));
        this.reapplyPosition();
        this.refreshDimensions();
    }

    public void setOneTime() {
        this.oneTimeDamage = true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.entityData.get(SIZE));
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    public boolean canStartDamage() {
        return !this.oneTimeDamage && this.livingTicks % 3 == 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            double height = this.getBoundingBox().getYsize();
            for (Vec3 vec3 : BASE) {
                vec3 = vec3.scale(this.entityData.get(SIZE));
                Vec3 rot = MathUtils.rotate(MathUtils.normalY, vec3, -Mth.DEG_TO_RAD * this.getYRot());
                for (double d = 0; d < height; d += 0.2) {
                    this.level.addParticle(ParticleTypes.CRIT, this.getX() + rot.x, this.getY() + rot.y + d, this.getZ() + rot.z, 0, 0, 0);
                }
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(2), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(SIZE, compound.getFloat("Size"));
        this.oneTimeDamage = compound.getBoolean("OneTime");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Size", this.entityData.get(SIZE));
        compound.putBoolean("OneTime", this.oneTimeDamage);
    }

    public enum Type {
        CHIMERA,
        SKELEFANG
    }
}
