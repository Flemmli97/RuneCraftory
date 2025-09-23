package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.List;

public class SporeEntity extends BaseDamageCloud {

    private static final List<Vector3f> PARTICLE_CIRCLE = MathUtils.rotatedVecs(new Vector3f(0.025f, 0.065f, 0), new Vector3f(0, 1, 0), -180, 160, 20);

    public SporeEntity(EntityType<? extends SporeEntity> type, Level level) {
        super(type, level);
    }

    public SporeEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.SPORE.get(), level, shooter);
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
        if (!this.level().isClientSide && this.livingTicks == 1) {
            this.level().broadcastEntityEvent(this, (byte) 64);
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), livingEntity, new DynamicDamage.Builder(this, this.getOwner()).element(ItemElement.EARTH).magic().noKnockback().hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        return super.damageBoundingBox().inflate(0.2, 0.5, 0.2);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) {
            for (Vector3f dir : PARTICLE_CIRCLE) {
                for (int i = 0; i < 3; i++) {
                    AdvancedParticleContainer.make(new DustParticleOptions(new Vector3f(168 / 255F, 227 / 255F, 86 / 255F), 1))
                            .addData(new ScaleData(0.25f))
                            .addData(new MotionData(dir.x() * 3, dir.y(), dir.z() * 3))
                            .addData(new ParticleMetaData(15, true, 0.2f))
                            .add(this.level(), this.getX(), this.getY() + this.getBbHeight() * 0.3, this.getZ());
                }
            }
        } else
            super.handleEntityEvent(id);
    }
}
