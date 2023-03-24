package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityPoisonNeedle extends BaseProjectile {

    public EntityPoisonNeedle(EntityType<? extends EntityPoisonNeedle> type, Level world) {
        super(type, world);
    }

    public EntityPoisonNeedle(Level world, LivingEntity shooter) {
        super(ModEntities.POISON_NEEDLE.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).withChangedAttribute(ModAttributes.RF_POISON.get(), 75), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }
}
