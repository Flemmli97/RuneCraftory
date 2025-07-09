package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityTornado extends BaseDamageCloud {

    public EntityTornado(EntityType<? extends EntityTornado> type, Level world) {
        super(type, world);
    }

    public EntityTornado(Level world, LivingEntity shooter) {
        super(ModEntities.TORNADO.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 80;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 3;
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(AdvancedProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getOwner() instanceof LivingEntity living) {
                if (this.random.nextBoolean()) {
                    EntityWindBlade wind = new EntityWindBlade(this.level(), living);
                    wind.setPos(this.getX(), this.getRandomY(), this.getZ());
                    wind.setDamageMultiplier(this.damageMultiplier);
                    wind.setType(EntityWindBlade.Type.PLAIN);
                    wind.shoot(this.random.nextDouble() - 0.5, this.random.nextDouble() * 0.3 - 0.15, this.random.nextDouble() - 0.5, 0.6f, 3);
                    this.level().addFreshEntity(wind);
                }
            }
        } else {
            Vec3 delta = this.getDeltaMovement().normalize().scale(0.5);
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(new ColoredParticleData4f.Builder((49 + this.random.nextInt(25)) / 255F, (150 + this.random.nextInt(40)) / 255F, (18 + this.random.nextInt(25)) / 255F, 1)
                                .withScale(0.2f).circle(0, 10).expandCircle(0.015f)
                                .withOffset(this.random.nextInt(360))
                                .withSpeed(0.2f).build(ModParticles.TORNADO.get()),
                        this.position().x() + delta.x() + this.random.nextDouble() * 0.6 - 0.3,
                        this.position().y() - 0.1,
                        this.position().z() + delta.z() + this.random.nextDouble() * 0.6 - 0.3,
                        0, 0, 0);
            }
        }
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        this.setDeltaMovement(motion.scale(0.98));
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), livingEntity, new DynamicDamage.Builder(this, this.getOwner()).element(EnumElement.WIND).magic().noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0.2, 0.5, 0.2);
    }
}
