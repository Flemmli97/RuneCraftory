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

public class EntityStarfall extends BaseProjectile {

    public EntityStarfall(EntityType<? extends EntityStarfall> type, Level world) {
        super(type, world);
    }

    public EntityStarfall(Level world, LivingEntity shooter) {
        super(ModEntities.STARFALL.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0, 0.01, 0));
        if (this.level.isClientSide) {
            float width = this.getBbWidth() / 2;
            for (int i = 0; i < 4; i++)
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 49 / 255f, 103 / 255f, 189 / 255f, 0.7f, 1),
                        this.getX() + this.random.nextGaussian() * width, this.getY() + this.random.nextGaussian() * width, this.getZ() + this.random.nextGaussian() * width, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
        }
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.LIGHT).noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
    }
}
