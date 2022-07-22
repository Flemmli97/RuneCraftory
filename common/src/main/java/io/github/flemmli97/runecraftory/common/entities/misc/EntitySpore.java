package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntitySpore extends EntityDamageCloud {

    private static final List<Vector3f> particleCircle = RayTraceUtils.rotatedVecs(new Vec3(0.025, 0.065, 0), new Vec3(0, 1, 0), -180, 160, 20);

    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntitySpore(EntityType<? extends EntitySpore> type, Level world) {
        super(type, world);
    }

    public EntitySpore(Level world, LivingEntity shooter) {
        super(ModEntities.spore.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int maxHitCount() {
        return 1;
    }

    @Override
    public int livingTickMax() {
        return 20;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 4;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.livingTicks == 1) {
            this.level.broadcastEntityEvent(this, (byte) 64);
        }
    }

    @Override
    protected boolean canHit(LivingEntity entity) {
        return super.canHit(entity) && (this.pred == null || this.pred.test(entity));
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damage(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0, 0.1, 0);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) {
            for (Vector3f dir : particleCircle) {
                for (int i = 0; i < 3; i++)
                    this.level.addParticle(new ColoredParticleData(ModParticles.sinkingDust.get(), 168 / 255F, 227 / 255F, 86 / 255F, 1), this.getX(), this.getY() + this.getBbHeight() * 0.3, this.getZ(), dir.x(), dir.y(), dir.z());
            }
        } else
            super.handleEntityEvent(id);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
