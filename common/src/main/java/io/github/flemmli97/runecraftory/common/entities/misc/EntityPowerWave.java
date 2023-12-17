package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityPowerWave extends BaseProjectile {

    private final List<UUID> attackedEntities = new ArrayList<>();

    public EntityPowerWave(EntityType<? extends EntityPowerWave> type, Level level) {
        super(type, level);
    }

    public EntityPowerWave(Level level, LivingEntity shooter) {
        super(ModEntities.POWER_WAVE.get(), level, shooter);
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
        if (this.level.isClientSide) {
            for (int i = 0; i < 4; i++)
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 207 / 255F, 194 / 255F, 60 / 255F, 0.8f, (float) (3.5 + this.random.nextGaussian() * 0.5)),
                        this.getX() + this.random.nextGaussian() * 0.15, this.getY(), this.getZ() + this.random.nextGaussian() * 0.15, 0, 0.15 + this.random.nextGaussian() * 0.03, 0);
        } else {
            List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5).expandTowards(0, 1.5, 0), this::canHit);
            for (LivingEntity living : targets) {
                this.checkedEntities.add(living.getUUID());
                if (!this.attackedEntities.contains(living.getUUID()) && CombatUtils.damageWithFaintAndCrit(this.getOwner(), living, new CustomDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(4), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
                    this.attackedEntities.add(living.getUUID());
                }
            }
        }
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(4), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
    }
}
