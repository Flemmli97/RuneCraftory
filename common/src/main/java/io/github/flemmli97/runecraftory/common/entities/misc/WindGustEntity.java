package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.tenshilib.common.entity.BeamEntity;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.function.Predicate;

public class WindGustEntity extends BeamEntity {

    private Vec3 pMotion, up, side;
    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    public WindGustEntity(EntityType<? extends WindGustEntity> type, Level world) {
        super(type, world);
    }

    public WindGustEntity(Level world, LivingEntity shooter) {
        super(RuneCraftoryEntities.GUST.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
    }

    @Override
    public float getRange() {
        return 8;
    }

    @Override
    public float radius() {
        return 2.5f;
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
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            for (int i = 0; i < 4; i++) {
                double upScale = this.random.nextDouble() * 2 - 1;
                double sideScale = this.random.nextDouble() * 2 - 1;
                Vec3 ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale));
                this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.WIND.get(), 255 / 255F, 255 / 255F, 255 / 255F, 1, 0.15f), ppos.x(), ppos.y(), ppos.z(), this.pMotion.x(), this.pMotion.y(), this.pMotion.z());
            }
        }
    }

    @Override
    public HitResult getHitRay() {
        HitResult res = super.getHitRay();
        Vec3 dir = res.getLocation().subtract(this.getEyePosition()).normalize();
        this.up = this.getUpVector(1).normalize().scale(this.radius());
        Vector3d rot = new Vector3d(dir.x(), dir.y(), dir.z())
                .rotateAxis(90 * Mth.DEG_TO_RAD, this.up.x(), this.up.y(), this.up.z());
        this.side = new Vec3(rot.x(), rot.y(), rot.z()).normalize();
        this.pMotion = dir.scale(0.5);
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
