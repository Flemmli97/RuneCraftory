package com.flemmli97.runecraftory.common.entities.misc;

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

public class EntitySpore extends EntityDamageCloud {

    private static final List<Vector3f> particleCircle = RayTraceUtils.rotatedVecs(new Vector3d(0.025, 0.065, 0), new Vector3d(0, 1, 0), -180, 160, 20);

    private Predicate<LivingEntity> pred;

    public EntitySpore(EntityType<? extends EntitySpore> type, World world) {
        super(type, world);
    }

    public EntitySpore(World world, LivingEntity shooter) {
        super(ModEntities.spore.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
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
    protected boolean canHit(LivingEntity entity) {
        return super.canHit(entity) && (this.pred == null || this.pred.test(entity));
    }

    @Override
    protected AxisAlignedBB damageBoundingBox() {
        return super.damageBoundingBox().grow(0, 0.1, 0);
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damage(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(20).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()), null);
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
            for (Vector3f dir : particleCircle) {
                for (int i = 0; i < 3; i++)
                    this.world.addParticle(new ColoredParticleData(ModParticles.sinkingDust.get(), 168 / 255F, 227 / 255F, 86 / 255F, 1), this.getX(), this.getY() + this.getHeight() * 0.3, this.getZ(), dir.getX(), dir.getY(), dir.getZ());
            }
        } else
            super.handleStatusUpdate(id);
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
