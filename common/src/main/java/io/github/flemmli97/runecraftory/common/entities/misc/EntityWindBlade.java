package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityWindBlade extends EntityProjectile {

    private Predicate<LivingEntity> pred;

    private Vec3 target;
    private float damageMultiplier = 1;

    public EntityWindBlade(EntityType<? extends EntityWindBlade> type, Level world) {
        super(type, world);
    }

    public EntityWindBlade(Level world, LivingEntity shooter) {
        super(ModEntities.windBlade.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setTarget(Entity entity) {
        this.target = entity.getEyePosition(1);
    }

    public void setTarget(Vec3 vec) {
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
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).element(EnumElement.WIND).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.target == null) {
                List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(16).expandTowards(this.getDeltaMovement()), e -> !(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e));
                double distSq = Double.MAX_VALUE;
                Entity res = null;
                for (Entity e : list) {
                    if (e.isPickable() && e.isAttackable()) {
                        if (e.distanceToSqr(this) < distSq) {
                            res = e;
                        }
                    }
                }
                if (res != null)
                    this.target = res.getEyePosition(1);
            }
            if (this.target != null) {
                Vec3 dir = this.target.subtract(this.position()).normalize().scale(0.15);
                this.push(dir.x(), dir.y(), dir.z());
            }
        }
    }

    @Override
    public Entity getOwner() {
        Entity living = super.getOwner();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).hitPred;
        return living;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
