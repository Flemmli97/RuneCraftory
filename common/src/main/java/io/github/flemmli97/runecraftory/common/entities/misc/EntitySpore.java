package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntitySpore extends BaseDamageCloud {

    private static final List<Vector3f> PARTICLE_CIRCLE = RayTraceUtils.rotatedVecs(new Vec3(0.025, 0.065, 0), new Vec3(0, 1, 0), -180, 160, 20);

    public EntitySpore(EntityType<? extends EntitySpore> type, Level world) {
        super(type, world);
    }

    public EntitySpore(Level world, LivingEntity shooter) {
        super(ModEntities.SPORE.get(), world, shooter);
    }

    @Override
    public int maxHitCount() {
        return 1;
    }

    @Override
    public int livingTickMax() {
        return 20;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks > 4;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.livingTicks == 1) {
            this.level.broadcastEntityEvent(this, (byte) 64);
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damage(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0.2, 0.5, 0.2);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) {
            for (Vector3f dir : PARTICLE_CIRCLE) {
                for (int i = 0; i < 3; i++)
                    this.level.addParticle(new ColoredParticleData(ModParticles.sinkingDust.get(), 168 / 255F, 227 / 255F, 86 / 255F, 1), this.getX(), this.getY() + this.getBbHeight() * 0.3, this.getZ(), dir.x(), dir.y(), dir.z());
            }
        } else
            super.handleEntityEvent(id);
    }
}
