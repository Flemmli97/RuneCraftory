package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityMissile extends BaseProjectile {

    private Entity target;

    public EntityMissile(EntityType<? extends EntityMissile> type, Level world) {
        super(type, world);
    }

    public EntityMissile(Level world, LivingEntity shooter) {
        super(ModEntities.MISSILE.get(), world, shooter);
    }

    public void setTarget(Entity entity) {
        this.target = entity;
    }


    @Override
    public int livingTickMax() {
        return 120;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.target == null) {
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
                Vec3 dir = this.target.getEyePosition().subtract(this.position()).normalize();
                double angle = Math.acos(dir.dot(this.getDeltaMovement()) / (dir.length() * this.getDeltaMovement().length()));
                dir = dir.scale(Math.abs(angle) > 0.18 ? 0.03 : 0.06);
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
        Entity e = result.getEntity();
        if (e instanceof LivingEntity living) {
            if (living.getLastDamageSource() != null && living.getLastDamageSource().getDirectEntity() instanceof EntityMissile) {
                living.invulnerableTime = 10;
            }
        }
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(EnumElement.LIGHT).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(4, 0.5f), this);
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(4, 0.5f), this);
        this.discard();
    }
}
