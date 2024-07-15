package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

    public void shootAtEntity(Entity target, float velocity, float inaccuracy) {
        Vec3 targetPos = EntityUtil.getStraightProjectileTarget(this.position(), target);
        Vec3 dir = (new Vec3(targetPos.x() - this.getX(), targetPos.y() - this.getY(), targetPos.z() - this.getZ()));
        this.shoot(dir.x, dir.y, dir.z, velocity, inaccuracy);
    }

    public void shootFromRotation(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        float g = -Mth.sin((pitch + pitchOffset) * ((float) Math.PI / 180));
        float h = Mth.cos(yaw * ((float) Math.PI / 180)) * Mth.cos(pitch * ((float) Math.PI / 180));
        this.shoot(f, g, h, velocity, inaccuracy);
        Vec3 vec3 = shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.isOnGround() ? 0.0 : vec3.y, vec3.z));
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        double f = Math.sqrt(EntityProjectile.horizontalMag(vector3d));
        this.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.getOwner() instanceof LivingEntity living) {
                if (this.random.nextBoolean()) {
                    EntityWindBlade wind = new EntityWindBlade(this.level, living);
                    wind.setPos(this.getX(), this.getRandomY(), this.getZ());
                    wind.setDamageMultiplier(this.damageMultiplier);
                    wind.setType(EntityWindBlade.Type.PLAIN);
                    wind.shoot(this.random.nextDouble() - 0.5, this.random.nextDouble() * 0.3 - 0.15, this.random.nextDouble() - 0.5, 0.6f, 3);
                    this.level.addFreshEntity(wind);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                this.level.addParticle(new ColoredParticleData4f.Builder((49 + this.random.nextInt(25)) / 255F, (150 + this.random.nextInt(40)) / 255F, (18 + this.random.nextInt(25)) / 255F, 1)
                        .withScale(0.2f).circle(0.3f, 10).expandCircle(0.03f)
                        .withOffset(this.random.nextInt(360))
                        .withSpeed(0.23f).build(ModParticles.TORNADO.get()), this.position().x() + this.random.nextDouble() * 0.6 - 0.3, this.position().y() - 0.1, this.position().z() + this.random.nextDouble() * 0.6 - 0.3, 0, 0, 0);
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
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.WIND).magic().noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0.2, 0.5, 0.2);
    }
}
