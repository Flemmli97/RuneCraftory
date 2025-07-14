package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.tenshilib.common.entity.AdvancedProjectile;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GustRocksEntity extends BaseDamageCloud {

    private Vec3 up, side;

    public GustRocksEntity(EntityType<? extends GustRocksEntity> type, Level world) {
        super(type, world);
    }

    public GustRocksEntity(Level world, LivingEntity shooter) {
        super(RuneCraftoryEntities.GUST_ROCK.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 28;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 4;
    }

    public void setDirection(float pitch, float yaw) {
        this.setYRot(pitch);
        this.setXRot(yaw);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void setDirection(double x, double y, double z) {
        Vec3 vector3d = new Vec3(x, y, z);
        double f = Math.sqrt(AdvancedProjectile.horizontalMag(vector3d));
        this.setYRot((float) -(Mth.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vector3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.up == null) {
            this.up = new Vec3(0, 1, 0).scale(8);
            this.side = this.getLookAngle().yRot(90).normalize().scale(12);
        }
        Vec3 pos = this.position();
        if (this.level().isClientSide) {
            for (int i = 0; i < 16; i++) {
                double randX = (this.random.nextDouble() * 2 - 1) * 14;
                double randY = (this.random.nextDouble() * 2 - 1) * 14;
                double randZ = (this.random.nextDouble() * 2 - 1) * 14;
                Vec3 pos2 = pos.add(randX, randY, randZ);
                this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.WIND.get(), 255 / 255F, 255 / 255F, 255 / 255F, 1, 0.15f), pos2.x(), pos2.y(), pos2.z(), this.getLookAngle().x(), this.getLookAngle().y(), this.getLookAngle().z());
            }
        } else {
            for (int i = 0; i < 2; i++) {
                if (this.getOwner() instanceof LivingEntity living) {
                    ElementalBallEntity spellBall = new ElementalBallEntity(this.level(), living, ItemElement.EARTH);
                    spellBall.withMaxLivingTicks(40);
                    spellBall.setDamageMultiplier(this.damageMultiplier);
                    double upScale = this.random.nextDouble();
                    double sideScale = this.random.nextDouble() * 2 - 1;
                    Vec3 pos2 = pos.add(this.getLookAngle().scale(-8)).add(this.up.scale(upScale)).add(this.side.scale(sideScale));
                    spellBall.setPos(pos2.x(), pos2.y(), pos2.z());
                    spellBall.shoot(this.getLookAngle().x, -0.28, this.getLookAngle().z, 0.9f, 0);
                    this.level().addFreshEntity(spellBall);
                }
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        Vec3 mot = this.getLookAngle().scale(0.35);
        livingEntity.push(mot.x(), mot.y(), mot.z());
        livingEntity.hurtMarked = true;
        return false;
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(16, 16, 16);
    }
}
