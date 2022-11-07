package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityButterfly extends EntityProjectile {

    private Vec3 targetPos;
    private double length;
    private boolean turn;
    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 0.15f;

    public EntityButterfly(EntityType<? extends EntityButterfly> type, Level level) {
        super(type, level);
    }

    public EntityButterfly(Level level, double x, double y, double z) {
        super(ModEntities.butterfly.get(), level, x, y, z);
    }

    public EntityButterfly(Level level, LivingEntity thrower) {
        super(ModEntities.butterfly.get(), level, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (this.pred == null || (entity instanceof LivingEntity && this.pred.test((LivingEntity) entity)));
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
        if (owner != null)
            CombatUtils.applyTempAttribute(owner, ModAttributes.RFDRAIN.get(), 100);
        if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(3), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            if (owner != null)
                CombatUtils.removeAttribute(owner, ModAttributes.RFDRAIN.get());
            if (result.getEntity() instanceof LivingEntity)
                ((LivingEntity) result.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            return true;
        }
        if (owner != null)
            CombatUtils.removeAttribute(owner, ModAttributes.RFDRAIN.get());
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
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
