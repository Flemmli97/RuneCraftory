package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class PoisonNeedleEntity extends BaseProjectile {

    public PoisonNeedleEntity(EntityType<? extends PoisonNeedleEntity> type, Level world) {
        super(type, world);
    }

    public PoisonNeedleEntity(Level world, LivingEntity shooter) {
        super(RuneCraftoryEntities.POISON_NEEDLE.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(5).withChangedAttribute(RuneCraftoryAttributes.POISON.asHolder(), 30).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
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
