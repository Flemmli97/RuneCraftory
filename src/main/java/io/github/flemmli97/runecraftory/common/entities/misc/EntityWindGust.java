package io.github.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.tenshilib.common.entity.EntityBeam;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class EntityWindGust extends EntityBeam {

    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    private Vector3d pMotion, up, side;
    private double maxX, maxY, maxZ, minX, minY, minZ;

    public EntityWindGust(EntityType<? extends EntityWindGust> type, World world) {
        super(type, world);
    }

    public EntityWindGust(World world, LivingEntity shooter) {
        super(ModEntities.gust.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> ((BaseMonster) shooter).hitPred.test(e);
    }

    @Override
    public float radius() {
        return 1.5f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            Vector3d pos = this.getPositionVec();
            for (int i = 0; i < 2; i++) {
                double upScale = this.rand.nextDouble() * 2 - 1;
                double sideScale = this.rand.nextDouble() * 2 - 1;
                Vector3d ppos = pos.add(this.up.scale(upScale)).add(this.side.scale(sideScale));
                this.world.addParticle(new ColoredParticleData(ModParticles.wind.get(), 255 / 255F, 255 / 255F, 255 / 255F, 1, 0.15f), ppos.getX(), ppos.getY(), ppos.getZ(), this.pMotion.getX(), this.pMotion.getY(), this.pMotion.getZ());
            }
        }
    }

    @Override
    public RayTraceResult getHitRay() {
        RayTraceResult res = super.getHitRay();
        this.pMotion = res.getHitVec().subtract(this.getPositionVec()).normalize().scale(0.5);
        this.up = this.getUpVector(1).normalize().scale(this.radius());
        this.side = new Vector3d(RayTraceUtils.rotatedAround(this.pMotion, new Vector3f(up), 90))
                .normalize().scale(this.radius());
        return res;
    }

    @Override
    public void onImpact(EntityRayTraceResult entityRayTraceResult) {
        Entity e = entityRayTraceResult.getEntity();
        if (e instanceof LivingEntity) {
            Vector3d mot = this.hit.getHitVec().subtract(this.getPositionVec()).normalize().scale(0.3);
            e.addVelocity(mot.getX(), mot.getY(), mot.getZ());
            e.velocityChanged = true;
        }
    }

    @Override
    protected boolean check(Entity entity, Vector3d from, Vector3d to) {
        return super.check(entity, from, to) && (!(entity instanceof LivingEntity) || this.pred.test((LivingEntity) entity));
    }

    @Override
    public float getRange() {
        return 8;
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    public int attackCooldown() {
        return 1;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
