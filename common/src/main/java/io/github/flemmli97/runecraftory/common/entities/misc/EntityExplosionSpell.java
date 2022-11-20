package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;
import java.util.function.Predicate;

public class EntityExplosionSpell extends EntityProjectile {

    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntityExplosionSpell(EntityType<? extends EntityExplosionSpell> type, Level level) {
        super(type, level);
    }

    public EntityExplosionSpell(Level level, LivingEntity shooter) {
        super(ModEntities.explosion.get(), level, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
        this.tickCount = 5;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 246 / 255F, 52 / 255F, 52 / 255F, 0.5f, 3f), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity)) && super.canHit(entity);
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
        this.level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        this.remove(RemovalReason.KILLED);
        if (this.level instanceof ServerLevel serverLevel) {
            AABB area = new AABB(x - 0.5, y - 0.5, z + 0.5, x + 0.5, y + 0.5, z + 0.5).inflate(7);
            for (ServerPlayer player : serverLevel.players()) {
                if (!area.contains(player.getX(), player.getY(), player.getZ()))
                    continue;
                Platform.INSTANCE.sendToClient(new S2CScreenShake(8, 1), player);
            }
        }
    }

    protected void doExplosion(Entity hit) {
        if (hit != null)
            CombatUtils.damage(this.getOwner(), hit, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.FIRE).hurtResistant(5).knock(CustomDamage.KnockBackType.BACK).knockAmount(1), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
        List<Entity> list = this.level.getEntities(this, new AABB(-5, -5, -5, 5, 5, 5).move(this.position()));
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
            CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.FIRE).hurtResistant(5), true, true, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier * dmgPerc, null);
        }
        if (this.level instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 2, 1.0, 0.0, 0.0, 1);
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
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
