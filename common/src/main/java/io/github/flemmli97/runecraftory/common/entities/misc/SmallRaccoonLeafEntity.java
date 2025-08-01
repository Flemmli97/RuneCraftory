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

public class SmallRaccoonLeafEntity extends BaseProjectile {

    public SmallRaccoonLeafEntity(EntityType<? extends BaseProjectile> type, Level level) {
        super(type, level);
    }

    public SmallRaccoonLeafEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.SMALL_RACCOON_LEAF.get(), level, shooter);
        if (shooter.getBbHeight() > 2)
            this.setPos(this.getX(), shooter.getY() + shooter.getBbHeight() * 0.5, this.getZ());
    }

    @Override
    public int livingTickMax() {
        return 24;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(2).element(ItemElement.EARTH), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
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
