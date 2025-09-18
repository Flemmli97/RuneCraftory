package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.CirclingData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TornadoEntity extends BaseDamageCloud {

    public TornadoEntity(EntityType<? extends TornadoEntity> type, Level level) {
        super(type, level);
    }

    public TornadoEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.TORNADO.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 100;
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
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getOwner() instanceof LivingEntity living) {
                if (this.random.nextBoolean()) {
                    WindBladeEntity wind = new WindBladeEntity(this.level(), living);
                    wind.setPos(this.getX(), this.getRandomY(), this.getZ());
                    wind.setDamageMultiplier(this.damageMultiplier);
                    wind.setType(WindBladeEntity.Type.PLAIN);
                    wind.shoot(this.random.nextDouble() - 0.5, this.random.nextDouble() * 0.3 - 0.15, this.random.nextDouble() - 0.5, 0.6f, 3);
                    this.level().addFreshEntity(wind);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                AdvancedParticleContainer.make(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (49 + this.random.nextInt(25)) / 255F, (150 + this.random.nextInt(40)) / 255F, (18 + this.random.nextInt(25)) / 255F))
                        .addData(new ScaleData(0.17f + this.getRandom().nextFloat() * 0.05f))
                        .addData(new MotionData(this.getDeltaMovement().x(), 0.35, this.getDeltaMovement().z()))
                        .addData(new CirclingData(0, 0.07f, this.random.nextInt(360), 10 + this.getRandom().nextInt(5), MathUtils.NORMAL_Y))
                        .addData(new ParticleMetaData(17 + this.getRandom().nextInt(8), false, 0))
                        .build().add(this.level(), this.getRandomX(0.15),
                                this.getY(),
                                this.getRandomZ(0.15));
            }
        }
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        this.setDeltaMovement(motion.scale(0.99));
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), livingEntity, new DynamicDamage.Builder(this, this.getOwner()).element(ItemElement.WIND).magic().noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0.2, 0.5, 0.2);
    }
}
