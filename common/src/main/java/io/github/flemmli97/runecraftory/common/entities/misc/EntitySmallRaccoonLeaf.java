package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntitySmallRaccoonLeaf extends BaseProjectile {

    public EntitySmallRaccoonLeaf(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public EntitySmallRaccoonLeaf(Level world, LivingEntity shooter) {
        super(ModEntities.SMALL_RACCOON_LEAF.get(), world, shooter);
        if (shooter.getBbHeight() > 2)
            this.setPos(this.getX(), shooter.getY() + shooter.getBbHeight() * 0.5, this.getZ());
    }

    @Override
    public int livingTickMax() {
        return 18;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(2).element(EnumElement.EARTH), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (att)
            this.discard();
        return att;
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
