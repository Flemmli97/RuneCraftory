package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExplosionSpellEntity extends BaseProjectile {

    public ExplosionSpellEntity(EntityType<? extends ExplosionSpellEntity> type, Level level) {
        super(type, level);
    }

    public ExplosionSpellEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.EXPLOSION.get(), level, shooter);
        this.tickCount = 5;
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 dir = this.getDeltaMovement().scale(0.5);
            this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.LIGHT.get(), 246 / 255F, 52 / 255F, 52 / 255F, 0.5f, 3f), this.getX() + dir.x(), this.getY() + dir.y(), this.getZ() + dir.z(), 0, 0, 0);
            this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.LIGHT.get(), 246 / 255F, 52 / 255F, 52 / 255F, 0.5f, 3f), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0025f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        this.doExplosion(result.getEntity().getX(), result.getEntity().getY(), result.getEntity().getZ(), result.getEntity());
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.doExplosion(result.getLocation().x, result.getLocation().y, result.getLocation().z, null);
    }

    private void doExplosion(double x, double y, double z, Entity hit) {
        this.doExplosion(hit);
        this.level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        this.discard();
        S2CScreenShake.sendAround(this.level(), new Vec3(x, y, z), 16, 8, 2);
    }

    protected void doExplosion(Entity hit) {
        if (hit != null)
            CombatUtils.damageWithFaintAndCrit(this.getOwner(), hit, new DynamicDamage.Builder(this, this.getOwner()).magic().element(ItemElement.FIRE).hurtResistant(5).knock(DynamicDamage.KnockBackType.BACK).knockAmount(1), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
        List<Entity> list = this.level().getEntities(this, new AABB(-5, -5, -5, 5, 5, 5).move(this.position()));
        for (Entity e : list) {
            double dist;
            if ((dist = e.distanceToSqr(this)) > 25 || (e != hit && !this.canHit(e)))
                continue;
            float dmgPerc;
            if (dist > 20)
                dmgPerc = 0.6f;
            else if (dist > 13)
                dmgPerc = 0.8f;
            else
                dmgPerc = 1;
            CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new DynamicDamage.Builder(this, this.getOwner()).magic().element(ItemElement.FIRE).hurtResistant(5).knock(DynamicDamage.KnockBackType.BACK).knockAmount(1), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier * dmgPerc, null);
        }
        if (this.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 2, 1.0, 0.0, 0.0, 1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }
}
