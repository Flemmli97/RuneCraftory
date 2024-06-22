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

public class EntityBoneNeedle extends BaseProjectile {

    public EntityBoneNeedle(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public EntityBoneNeedle(Level world, LivingEntity shooter) {
        super(ModEntities.BONE_NEEDLE.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean res = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.EARTH).hurtResistant(0).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (res)
            this.discard();
        return res;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }
}
