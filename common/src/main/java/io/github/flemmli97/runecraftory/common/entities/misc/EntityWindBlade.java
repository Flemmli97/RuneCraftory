package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.platform.EventCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityWindBlade extends BaseProjectile {

    private Entity target;
    private Type type = Type.HOMING;

    public EntityWindBlade(EntityType<? extends EntityWindBlade> type, Level world) {
        super(type, world);
    }

    public EntityWindBlade(Level world, LivingEntity shooter) {
        super(ModEntities.WIND_BLADE.get(), world, shooter);
    }

    public void setTarget(Entity entity) {
        this.target = entity;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean isPiercing() {
        return this.type == Type.PIERCING;
    }

    @Override
    public int livingTickMax() {
        return this.isPiercing() ? 80 : 30;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if ((this.target == null || !this.target.isAlive()) && this.type == Type.HOMING) {
                List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(16).expandTowards(this.getDeltaMovement()), e -> {
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
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(this.type == Type.PLAIN ? 2 : 10).element(EnumElement.WIND).projectile(), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
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
        BlockHitResult raytraceresult = this.level.clip(new ClipContext(pos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity tileentity = this.level.getBlockEntity(blockpos);
                if (tileentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity) tileentity);
                }
            } else if (!EventCalls.INSTANCE.projectileHitCall(this, raytraceresult)) {
                this.onBlockHit(raytraceresult);
            }
        }
    }

    public enum Type {
        PLAIN,
        HOMING,
        PIERCING,
    }
}
