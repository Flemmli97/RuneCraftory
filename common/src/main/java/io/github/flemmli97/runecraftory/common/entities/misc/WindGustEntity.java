package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.common.entity.BeamEntity;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class WindGustEntity extends BeamEntity {

    private Vec3 pMotion, up, side;
    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    public WindGustEntity(EntityType<? extends WindGustEntity> type, Level level) {
        super(type, level);
    }

    public WindGustEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.GUST.get(), level, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
    }

    @Override
    public float getRange() {
        return 8;
    }

    @Override
    public float radius() {
        return 3;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void updateHitDetectBox() {
        super.updateHitDetectBox();
        if (!this.level().isClientSide)
            S2CAttackDebug.sendDebugPacket(this.hitObb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            for (int i = 0; i < 4; i++) {
                double upScale = this.random.nextDouble() * 2 - 1;
                double sideScale = this.random.nextDouble() * 2 - 1;
                Vec3 ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale));
                AdvancedParticleContainer.make(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xffffffff))
                        .addData(new MotionData(this.pMotion.x(), this.pMotion.y(), this.pMotion.z()))
                        .addData(new ScaleData(0.2f))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), ppos.x(), ppos.y(), ppos.z());
            }
        }
    }

    @Override
    public HitResult getHitRay() {
        HitResult res = super.getHitRay();
        Vec3 dir = res.getLocation().subtract(this.getEyePosition()).normalize();
        this.up = this.calculateViewVector(this.getXRot() - 90, this.getYRot()).scale(this.radius());
        this.side = dir.cross(this.up).normalize().scale(this.radius());
        this.pMotion = dir.scale(0.3);
        return res;
    }

    @Override
    protected boolean check(Entity e, Predicate<AABB> intersects) {
        return super.check(e, intersects) && (!(e instanceof LivingEntity living) || this.pred.test(living));
    }

    @Override
    public void onImpact(EntityHitResult entityRayTraceResult) {
        Entity e = entityRayTraceResult.getEntity();
        if (e instanceof LivingEntity) {
            Vec3 mot = this.hit.getLocation().subtract(this.position()).normalize().scale(0.3);
            e.push(mot.x(), mot.y(), mot.z());
            e.hurtMarked = true;
        }
    }

    @Override
    public boolean canStartDamage() {
        return true;
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
