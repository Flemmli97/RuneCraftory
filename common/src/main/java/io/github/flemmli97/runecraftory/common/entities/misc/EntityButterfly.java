package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityButterfly extends BaseProjectile {

    public EntityButterfly(EntityType<? extends EntityButterfly> type, Level level) {
        super(type, level);
        this.damageMultiplier = 0.15f;
    }

    public EntityButterfly(Level level, LivingEntity thrower) {
        super(ModEntities.BUTTERFLY.get(), level, thrower);
        this.damageMultiplier = 0.15f;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
        if (owner != null)
            CombatUtils.applyTempAttribute(owner, ModAttributes.RF_DRAIN.get(), 100);
        if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(3), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            if (owner != null)
                CombatUtils.removeAttribute(owner, ModAttributes.RF_DRAIN.get());
            if (result.getEntity() instanceof LivingEntity)
                ((LivingEntity) result.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            return true;
        }
        if (owner != null)
            CombatUtils.removeAttribute(owner, ModAttributes.RF_DRAIN.get());
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
    }
}
