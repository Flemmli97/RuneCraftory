package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntityThiccLightningBolt extends EntityProjectile {

    private Predicate<LivingEntity> pred;

    public EntityThiccLightningBolt(EntityType<? extends EntityThiccLightningBolt> type, Level world) {
        super(type, world);
    }

    public EntityThiccLightningBolt(Level world, LivingEntity shooter) {
        super(ModEntities.lightningOrbBolt.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
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
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity));
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        return CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(2).element(EnumElement.WIND).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()), null);//RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for (int i = 0; i < 6; i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 38 / 255F, 133 / 255F, 222 / 255F, 0.2f, 3f), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
        } else if (this.livingTicks % 13 == 0) {
            this.checkedEntities.clear();
            this.attackedEntities.clear();
        }
    }

    @Override
    public Entity getOwner() {
        Entity living = super.getOwner();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).hitPred;
        return living;
    }
}
