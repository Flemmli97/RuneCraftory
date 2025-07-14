package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class BoneNeedleEntity extends BaseProjectile {

    public BoneNeedleEntity(EntityType<? extends BaseProjectile> type, Level world) {
        super(type, world);
    }

    public BoneNeedleEntity(Level world, LivingEntity shooter) {
        super(RuneCraftoryEntities.BONE_NEEDLE.get(), world, shooter);
    }

    @Override
    public int livingTickMax() {
        return 30;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean res = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).element(ItemElement.EARTH).hurtResistant(0).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
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
