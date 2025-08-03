package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class DarkBulletEntity extends BaseProjectile {

    public DarkBulletEntity(EntityType<? extends DarkBulletEntity> type, Level level) {
        super(type, level);
    }

    public DarkBulletEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.DARK_BULLET.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 20;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().element(ItemElement.DARK).hurtResistant(3).projectile(), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }
}
