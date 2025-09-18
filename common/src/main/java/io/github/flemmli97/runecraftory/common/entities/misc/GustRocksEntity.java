package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GustRocksEntity extends BaseBeam {

    private Vec3 up, side;

    public GustRocksEntity(EntityType<? extends GustRocksEntity> type, Level level) {
        super(type, level);
    }

    public GustRocksEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.GUST_ROCK.get(), level, shooter);
        this.setPos(shooter.getX(), shooter.getY() + shooter.getBbHeight() + 2, shooter.getZ());
    }

    @Override
    public float getRange() {
        return 20;
    }

    @Override
    public float radius() {
        return 9;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 4;
    }

    @Override
    public void updateHitDetectBox() {
        this.hitObb = new OrientedBoundingBox(new AABB(-this.radius(), -this.radius() * 0.5, 0, this.radius(), this.radius(), this.getRange()),
                this.getYRot(), -this.getXRot(), this.position());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.up == null) {
            this.up = this.calculateViewVector(this.getXRot() - 90, this.getYRot()).scale(this.radius());
            this.side = this.getLookAngle().cross(this.up).normalize().scale(this.radius());
        }
        Vec3 pos = this.position();
        if (this.level().isClientSide) {
            Vec3 look = this.getLookAngle().scale(6);
            for (int i = 0; i < 20; i++) {
                double randX = (this.random.nextDouble() * 2 - 1) * this.radius() - look.x();
                double randY = (this.random.nextDouble() * 2 - 1) * this.radius() - look.y();
                double randZ = (this.random.nextDouble() * 2 - 1) * this.radius() - look.z();
                Vec3 pos2 = pos.add(randX, randY, randZ);
                AdvancedParticleContainer.make(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xffffffff))
                        .addData(new MotionData(this.getLookAngle().x(), this.getLookAngle().y(), this.getLookAngle().z()))
                        .addData(new ScaleData(0.3f))
                        .addData(new ParticleMetaData(30, false, 0))
                        .build().add(this.level(), pos2.x(), pos2.y(), pos2.z());
            }
        } else {
            for (int i = 0; i < 2; i++) {
                if (this.getOwner() instanceof LivingEntity living) {
                    ElementalBallEntity spellBall = new ElementalBallEntity(this.level(), living, ItemElement.EARTH);
                    spellBall.withMaxLivingTicks(60);
                    spellBall.setDamageMultiplier(this.damageMultiplier);
                    double upScale = this.random.nextDouble() * 1.5 - 0.5;
                    double sideScale = this.random.nextDouble() * 2 - 1;
                    Vec3 pos2 = pos.add(this.getLookAngle().scale(-8)).add(this.up.scale(upScale)).add(this.side.scale(sideScale));
                    spellBall.setPos(pos2.x(), pos2.y(), pos2.z());
                    spellBall.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 0.9f, 0);
                    this.level().addFreshEntity(spellBall);
                }
            }
        }
    }

    @Override
    public void onImpact(EntityHitResult result) {
        Vec3 mot = this.getLookAngle().scale(0.31);
        result.getEntity().push(mot.x(), mot.y(), mot.z());
        result.getEntity().hurtMarked = true;
    }
}
