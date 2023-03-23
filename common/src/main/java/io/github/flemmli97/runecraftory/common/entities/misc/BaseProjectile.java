package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.TargetableOpponent;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class BaseProjectile extends EntityProjectile {

    private Predicate<LivingEntity> pred = e -> !e.getUUID().equals(this.getOwnerUUID());
    protected float damageMultiplier = 1;

    public BaseProjectile(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public BaseProjectile(EntityType<? extends BaseProjectile> type, Level world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    public BaseProjectile(EntityType<? extends BaseProjectile> type, Level world, LivingEntity shooter) {
        super(type, world, shooter);
        if (shooter instanceof TargetableOpponent tO)
            this.pred = tO.validTargetPredicate();
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Nullable
    public Predicate<LivingEntity> getHitPredicate() {
        return this.pred;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity)) && super.canHit(entity);
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

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof TargetableOpponent tO)
            this.pred = tO.validTargetPredicate();
        return owner;
    }
}
