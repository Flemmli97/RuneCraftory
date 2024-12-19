package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.TargetableOpponent;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public abstract class BaseBeam extends EntityBeam {

    private Predicate<LivingEntity> pred = e -> !e.getUUID().equals(this.getOwnerUUID());
    protected float damageMultiplier = 1;

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world) {
        super(type, world);
    }

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world, LivingEntity shooter) {
        super(type, world, shooter);
        if (shooter instanceof TargetableOpponent targetableOpponent)
            this.pred = targetableOpponent.validTargetPredicate();
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected boolean check(Entity e, Predicate<AABB> intersects) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, intersects);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof TargetableOpponent targetableOpponent)
            this.pred = targetableOpponent.validTargetPredicate();
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
