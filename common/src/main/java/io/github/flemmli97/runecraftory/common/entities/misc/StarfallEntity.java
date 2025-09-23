package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class StarfallEntity extends BaseProjectile {

    public StarfallEntity(EntityType<? extends StarfallEntity> type, Level level) {
        super(type, level);
    }

    public StarfallEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.STARFALL.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0, 0.01, 0));
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(49 / 255f, 103 / 255f, 189 / 255f, 0.8f))
                        .addData(new ScaleData(0.25f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                        .addData(new ParticleMetaData(20, true, 0.2f))
                        .add(this.level(), this.getRandomX(1), this.getRandomY(), this.getRandomZ(1));
            }
        }
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).element(ItemElement.LIGHT).noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (!this.isAlive()) {
            return null;
        }
        return getEntityHitResult(this, to, this::canHit);
    }

    private static EntityHitResult getEntityHitResult(AdvancedProjectile projectile, Vec3 to, Predicate<Entity> pred) {
        double dY = to.y() - projectile.getY();
        AABB bb = projectile.getBoundingBox().expandTowards(0, dY, 0);
        for (Entity e : projectile.level().getEntities(projectile, bb.inflate(1), pred)) {
            if (e.getBoundingBox().inflate(0.3).intersects(bb))
                return new EntityHitResult(e);
        }
        return null;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
    }
}
