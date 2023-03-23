package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityBigPlate extends BaseProjectile {

    private boolean hitSomething;

    public EntityBigPlate(EntityType<? extends EntityBigPlate> type, Level world) {
        super(type, world);
    }

    public EntityBigPlate(Level world, LivingEntity shooter) {
        super(ModEntities.BIG_PLATE.get(), world, shooter);
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (!this.isAlive()) {
            return null;
        }
        return getEntityHitResult(this, from, to, this::canHit);
    }

    private static EntityHitResult getEntityHitResult(EntityProjectile projectile, Vec3 from, Vec3 to, Predicate<Entity> pred) {
        double dY = to.y() - projectile.getY();
        AABB bb = projectile.getBoundingBox().expandTowards(0, dY, 0);
        for (Entity e : projectile.level.getEntities(projectile, bb.inflate(1), pred)) {
            if (e.getBoundingBox().inflate(0.3).intersects(bb))
                return new EntityHitResult(e);
        }
        return null;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5), true, false, CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (!this.hitSomething) {
            this.level.playSound(null, result.getEntity().blockPosition(), SoundEvents.ANVIL_LAND, this.getSoundSource(), 1.0f, 0.5f);
            this.hitSomething = true;
        }
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.hitSomething) {
            this.level.playSound(null, blockHitResult.getBlockPos(), SoundEvents.ANVIL_LAND, this.getSoundSource(), 1.0f, 0.5f);
        }
        this.discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.hitSomething = compound.getBoolean("HitSomething");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HitSomething", this.hitSomething);
    }
}
