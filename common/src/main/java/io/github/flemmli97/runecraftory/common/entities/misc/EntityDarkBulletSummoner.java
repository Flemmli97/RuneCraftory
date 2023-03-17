package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityDarkBulletSummoner extends ProjectileSummonHelperEntity {

    public EntityDarkBulletSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityDarkBulletSummoner(Level level, LivingEntity caster) {
        super(ModEntities.DARK_BULLET_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 30;
    }

    @Override
    protected void summonProjectiles() {
        if (this.random.nextBoolean())
            return;
        EntityDarkBullet fly = new EntityDarkBullet(this.level, this.getOwner());
        fly.setPos(this.getX(), this.getY(), this.getZ());
        fly.shootAtPosition(this.targetX, this.targetY, this.targetZ, 1.2f, 0);
        fly.setDamageMultiplier(this.damageMultiplier);
        fly.setPos(fly.getX() + this.random.nextFloat() * 1.3 - 0.65, fly.getY() + this.random.nextFloat() * 0.05 - 0.1, fly.getZ() + this.random.nextFloat() * 1.3 - 0.65);
        this.level.addFreshEntity(fly);
    }
}
