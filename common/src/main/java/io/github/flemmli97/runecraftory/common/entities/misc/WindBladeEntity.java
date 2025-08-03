package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WindBladeEntity extends BaseProjectile {

    private Entity target;
    private Type type = Type.HOMING;
    private int livingTickMax = 70;

    public WindBladeEntity(EntityType<? extends WindBladeEntity> type, Level level) {
        super(type, level);
    }

    public WindBladeEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.WIND_BLADE.get(), level, shooter);
    }

    public void setTarget(Entity entity) {
        this.target = entity;
    }

    public void setType(Type type) {
        this.type = type;
        this.livingTickMax = this.isPiercing() ? 70 : 40;
    }

    public void maxTicks(int maxTicks) {
        this.livingTickMax = maxTicks;
    }

    @Override
    public boolean isPiercing() {
        return this.type == Type.PIERCING;
    }

    @Override
    public int livingTickMax() {
        return this.livingTickMax;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if ((this.target == null || !this.target.isAlive()) && this.type == Type.HOMING) {
                List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(16).expandTowards(this.getDeltaMovement()), e -> {
                    if (!e.isPickable() || !e.isAttackable())
                        return false;
                    if (e.equals(this.getOwner()) || (e instanceof OwnableEntity ownable && ownable.getOwner() == this.getOwner())) {
                        return false;
                    }
                    return this.canHit(e);
                });
                double distSq = Double.MAX_VALUE;
                Entity res = null;
                for (Entity e : list) {
                    if (e.distanceToSqr(this) < distSq) {
                        res = e;
                    }
                }
                if (res != null)
                    this.target = res;
            }
            if (this.target != null) {
                Vec3 dir = this.target.getEyePosition().subtract(this.position()).normalize().scale(0.1);
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(dir));
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(this.type == Type.PLAIN ? 2 : 10).element(ItemElement.WIND).projectile(), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null)) {
            if (!this.isPiercing())
                this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        if (!this.isPiercing())
            this.discard();
        else {
            Vec3 newMot;
            Vec3 mot = this.getDeltaMovement();
            switch (blockRayTraceResult.getDirection()) {
                case DOWN, UP -> newMot = new Vec3(mot.x(), -mot.y(), mot.z());
                case WEST, EAST -> newMot = new Vec3(-mot.x(), mot.y(), mot.z());
                default -> newMot = new Vec3(mot.x(), mot.y(), -mot.z());
            }
            if (!blockRayTraceResult.isInside())
                this.setPos(blockRayTraceResult.getLocation());
            this.setDeltaMovement(newMot);
            //If it hits a corner and its fast enough it can skip a correct bounce at that place. To fix we manually do a block collision check again
            this.doBlockCollision();
        }
    }

    private void doBlockCollision() {
        Vec3 pos = this.position();
        Vec3 to = pos.add(this.getDeltaMovement());
        BlockHitResult raytraceresult = this.level().clip(new ClipContext(pos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() == HitResult.Type.BLOCK && !TenshiLibCrossPlat.INSTANCE.projectileImpactEvent(this, raytraceresult)) {
            BlockPos blockpos = raytraceresult.getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            blockstate.onProjectileHit(this.level(), blockstate, raytraceresult, this);
            this.onBlockHit(raytraceresult);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setType(Type.values()[compound.getInt("Type")]);
        this.maxTicks(compound.getInt("MaxTicks"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Type", this.type.ordinal());
        compound.putInt("MaxTicks", this.livingTickMax());
    }

    public enum Type {
        PLAIN,
        HOMING,
        PIERCING,
    }
}
