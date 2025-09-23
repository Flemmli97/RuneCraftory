package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThiccLightningBoltEntity extends BaseProjectile {

    public ThiccLightningBoltEntity(EntityType<? extends ThiccLightningBoltEntity> type, Level level) {
        super(type, level);
    }

    public ThiccLightningBoltEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.LIGHTNING_ORB_BOLT.get(), level, shooter);
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
        return 70;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(38 / 255F, 133 / 255F, 222 / 255F, 0.8f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                        .addData(new ScaleData(0.25f))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), this.getRandomX(0.5), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(0.5));
            }
            for (int i = 0; i < 8; i++) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHTNING.get())
                        .addData(new MotionData(this.getRandom().nextGaussian() * 0.03, Math.abs(this.getRandom().nextGaussian() * 0.05), this.getRandom().nextGaussian() * 0.03))
                        .add(this.level(), this.getRandomX(0.5), this.getY(this.getRandom().nextDouble() * 0.5) + this.getBbHeight() * 0.4, this.getRandomZ(0.5));
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
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(4).element(ItemElement.WIND), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }
}
