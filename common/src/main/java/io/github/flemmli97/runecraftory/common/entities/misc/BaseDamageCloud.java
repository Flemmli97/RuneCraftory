package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.TargetableOpponent;
import io.github.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;

public abstract class BaseDamageCloud extends EntityDamageCloud {

    private Predicate<LivingEntity> pred = e -> !e.getUUID().equals(this.getOwnerUUID());
    protected float damageMultiplier = 1;

    public BaseDamageCloud(EntityType<? extends BaseDamageCloud> type, Level world) {
        super(type, world);
    }

    public BaseDamageCloud(EntityType<? extends BaseDamageCloud> type, Level world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    public BaseDamageCloud(EntityType<? extends BaseDamageCloud> type, Level world, LivingEntity shooter) {
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
    protected boolean canHit(LivingEntity entity) {
        return super.canHit(entity) && (this.pred == null || this.pred.test(entity));
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
