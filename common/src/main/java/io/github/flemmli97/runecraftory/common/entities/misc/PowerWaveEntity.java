package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerWaveEntity extends BaseProjectile {

    private final List<UUID> attackedEntities = new ArrayList<>();

    public PowerWaveEntity(EntityType<? extends PowerWaveEntity> type, Level level) {
        super(type, level);
    }

    public PowerWaveEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.POWER_WAVE.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 15;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 4; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(207 / 255F, 194 / 255F, 60 / 255F, 0.5f))
                        .addData(new MotionData(0, 0.15 + this.random.nextGaussian() * 0.03, 0))
                        .addData(new ScaleData((float) (0.4 + this.random.nextGaussian() * 0.2)))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), this.getX() + this.random.nextGaussian() * 0.15, this.getY(), this.getZ() + this.random.nextGaussian() * 0.15);
            }
        } else {
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5).expandTowards(0, 1.5, 0), this::canHit);
            for (LivingEntity living : targets) {
                this.checkedEntities.add(living.getUUID());
                if (!this.attackedEntities.contains(living.getUUID()) && CombatUtils.damageWithFaintAndCrit(this.getOwner(), living, new DynamicDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(4), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
                    this.attackedEntities.add(living.getUUID());
                }
            }
        }
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(4), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
    }
}
