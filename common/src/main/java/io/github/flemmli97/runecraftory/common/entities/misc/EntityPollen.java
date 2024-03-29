package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityPollen extends BaseDamageCloud {

    private static final List<Vector3f> POLLEN_BASE = RayTraceUtils.rotatedVecs(new Vec3(1, 0, 0), new Vec3(0, 1, 0), -180, 135, 45);
    private static final List<Vector3f> POLLEN_IND = RayTraceUtils.rotatedVecs(new Vec3(0.04, 0.07, 0), new Vec3(0, 1, 0), -180, 160, 20);

    public EntityPollen(EntityType<? extends EntityPollen> type, Level world) {
        super(type, world);
    }

    public EntityPollen(Level world, LivingEntity shooter) {
        super(ModEntities.POLLEN.get(), world, shooter);
    }

    private double radiusSqWithOffset(double offset) {
        double r = Math.max(0, this.getRadius() + offset);
        return r * r;
    }

    @Override
    public float radiusIncrease() {
        return 0.5f;
    }

    @Override
    public double maxRadius() {
        return 2;
    }

    @Override
    public int livingTickMax() {
        return 7;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks % 2 == 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.livingTicks == 1) {
            this.level.broadcastEntityEvent(this, (byte) 64);
        }
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        if (!super.canHit(e))
            return false;
        double distSq = e.distanceToSqr(this);
        double offset = e.getBbWidth() * 0.5 + 0.1;
        return distSq >= this.radiusSqWithOffset(-offset) && distSq <= this.radiusSqWithOffset(offset);
    }

    @Override
    protected boolean damageEntity(LivingEntity e) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(5).element(EnumElement.EARTH), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0, 0.3, 0);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) {
            for (Vector3f base : POLLEN_BASE) {
                for (Vector3f dir : POLLEN_IND) {
                    for (int i = 0; i < 3; i++)
                        this.level.addParticle(new ColoredParticleData(ModParticles.SINKING_DUST.get(), 10 / 255F, 138 / 255F, 12 / 255F, 1), this.getX() + base.x(), this.getY() + 0.05, this.getZ() + base.z(), dir.x() + base.x() * 0.02, dir.y(), dir.z() + base.z() * 0.02);
                }
            }
        } else
            super.handleEntityEvent(id);
    }
}
