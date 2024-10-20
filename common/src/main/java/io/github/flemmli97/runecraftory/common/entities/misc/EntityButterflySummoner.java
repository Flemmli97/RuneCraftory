package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityButterflySummoner extends ProjectileSummonHelperEntity {

    public EntityButterflySummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityButterflySummoner(Level level, LivingEntity caster) {
        super(ModEntities.BUTTERFLY_SUMMONER.get(), level, caster);
    }

    @Override
    protected void summonProjectiles() {
        EntityButterfly fly = new EntityButterfly(this.level, this.getOwner());
        fly.setDamageMultiplier(this.damageMultiplier);
        fly.setPos(fly.getX() + this.random.nextFloat() * 2 - 1, fly.getY() + this.random.nextFloat() * 0.05 - 0.1, fly.getZ() + this.random.nextFloat() * 2 - 1);
        fly.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.3f, 12);
        this.playSound(ModSounds.SPELL_GENERIC_POP.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        this.level.addFreshEntity(fly);
    }
}
