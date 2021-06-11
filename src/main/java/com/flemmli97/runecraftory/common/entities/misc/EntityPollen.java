package com.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModParticles;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class EntityPollen extends EntityDamageCloud {

    private static final List<Vector3f> pollenBase = RayTraceUtils.rotatedVecs(new Vector3d(1, 0, 0), new Vector3d(0, 1, 0), -180, 135, 45);
    private static final List<Vector3f> pollenInd = RayTraceUtils.rotatedVecs(new Vector3d(0.04, 0.07, 0), new Vector3d(0, 1, 0), -180, 160, 20);

    private Predicate<LivingEntity> pred;

    public EntityPollen(EntityType<? extends EntityPollen> type, World world) {
        super(type, world);
    }

    public EntityPollen(World world, LivingEntity shooter) {
        super(ModEntities.pollen.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
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
        if (!this.world.isRemote && this.livingTicks == 1) {
            this.world.setEntityState(this, (byte) 64);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 64) {
            for (Vector3f base : pollenBase) {
                for (Vector3f dir : pollenInd) {
                    for (int i = 0; i < 3; i++)
                        this.world.addParticle(new ColoredParticleData(ModParticles.sinkingDust.get(), 10 / 255F, 138 / 255F, 12 / 255F, 1), this.getPosX() + base.getX(), this.getPosY() + 0.05, this.getPosZ() + base.getZ(), dir.getX() + base.getX() * 0.02, dir.getY(), dir.getZ() + base.getZ() * 0.02);
                }
            }
        } else
            super.handleStatusUpdate(id);
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        if (!super.canHit(e))
            return false;
        double distSq = e.getDistanceSq(this);
        double offset = e.getWidth() * 0.5 + 0.1;
        return distSq >= this.radiusSqWithOffset(-offset) && distSq <= this.radiusSqWithOffset(offset) && (this.pred == null || this.pred.test(e));
    }

    private double radiusSqWithOffset(double offset) {
        double r = Math.max(0, this.getRadius() + offset);
        return r * r;
    }

    @Override
    protected boolean damageEntity(LivingEntity e) {
        return CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).element(EnumElement.EARTH).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()), null);
    }

    @Override
    protected AxisAlignedBB damageBoundingBox() {
        return super.damageBoundingBox().grow(0, 0.3, 0);
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
