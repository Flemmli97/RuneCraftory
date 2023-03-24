package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityThiccLightningBolt extends BaseProjectile {

    public EntityThiccLightningBolt(EntityType<? extends EntityThiccLightningBolt> type, Level world) {
        super(type, world);
    }

    public EntityThiccLightningBolt(Level world, LivingEntity shooter) {
        super(ModEntities.LIGHTNING_ORB_BOLT.get(), world, shooter);
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public float radius() {
        return 1;
    }

    @Override
    public int livingTickMax() {
        return 35;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for (int i = 0; i < 6; i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 38 / 255F, 133 / 255F, 222 / 255F, 0.2f, 3f), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
            for (int i = 0; i < 8; i++) {
                this.level.addParticle(ModParticles.lightning.get(), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, 0.05, 0.05, 0.05);
            }
        } else if (this.livingTicks % 13 == 0) {
            this.checkedEntities.clear();
            this.attackedEntities.clear();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(2).element(EnumElement.WIND), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }
}
