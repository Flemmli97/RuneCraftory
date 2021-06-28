package io.github.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class EntityWindBlade extends EntityProjectile {

    private Predicate<LivingEntity> pred;

    private Vector3d target;
    private float damageMultiplier = 1;

    public EntityWindBlade(EntityType<? extends EntityWindBlade> type, World world) {
        super(type, world);
    }

    public EntityWindBlade(World world, LivingEntity shooter) {
        super(ModEntities.windBlade.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setTarget(Entity entity) {
        this.target = entity.getEyePosition(1);
    }

    public void setTarget(Vector3d vec) {
        this.target = vec;
    }

    @Override
    public boolean isPiercing() {
        return false;
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (this.pred == null || (entity instanceof LivingEntity && this.pred.test((LivingEntity) entity)));
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult result) {
        if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).element(EnumElement.WIND).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            this.remove();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.remove();
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            for (int i = 0; i < 1; i++) {
                this.world.addParticle(new ColoredParticleData(ModParticles.cross.get(), 55 / 255F, 200 / 255F, 38 / 255F, 1, 2.3f), this.getPosX() + this.rand.nextGaussian() * 0.15, this.getPosY() + 0.35 + this.rand.nextGaussian() * 0.07, this.getPosZ() + this.rand.nextGaussian() * 0.15, this.rand.nextGaussian() * 0.01, Math.abs(this.rand.nextGaussian() * 0.03), this.rand.nextGaussian() * 0.01);
            }
        } else {
            if (this.target == null) {
                List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(16).expand(this.getMotion()), e -> e instanceof LivingEntity && this.pred != null ? this.pred.test((LivingEntity) e) : true);
                double distSq = Double.MAX_VALUE;
                Entity res = null;
                for (Entity e : list) {
                    if (e.canBeCollidedWith() && e.canBeAttackedWithItem()) {
                        if (e.getDistanceSq(this) < distSq) {
                            res = e;
                        }
                    }
                }
                if (res != null)
                    this.target = res.getEyePosition(1);
            }
            if (this.target != null) {
                Vector3d dir = this.target.subtract(this.getPositionVec()).normalize().scale(0.15);
                this.addVelocity(dir.getX(), dir.getY(), dir.getZ());
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        LivingEntity living = super.getOwner();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).hitPred;
        return living;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
