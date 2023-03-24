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
        CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(3)
                .withChangedAttribute(ModAttributes.RF_DRAIN.get(), 100);
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), builder, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            if (result.getEntity() instanceof LivingEntity livingTarget)
                livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
    }
}
