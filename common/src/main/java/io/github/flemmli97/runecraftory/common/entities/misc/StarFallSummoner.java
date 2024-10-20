package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class StarFallSummoner extends ProjectileSummonHelperEntity {

    public StarFallSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public StarFallSummoner(Level level, LivingEntity caster) {
        super(ModEntities.STARFALL_SUMMONER.get(), level, caster);
    }

    public void setMaxLivingTicks(int maxLivingTicks) {
        this.maxLivingTicks = maxLivingTicks;
    }

    @Override
    protected void summonProjectiles() {
        if (this.tickCount % 5 == 0) {
            for (int i = 0; i < 9; i++) {
                double randX = this.random.nextDouble() * 32 - 16;
                double randZ = this.random.nextDouble() * 32 - 16;
                if (randX * randX + randZ * randZ > 16 * 16)
                    continue;
                EntityStarfall proj = new EntityStarfall(this.level, this.getOwner());
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.setPos(this.getX() + randX, this.getY() + 11 + this.random.nextDouble() * 2, this.getZ() + randZ);
                this.level.addFreshEntity(proj);
            }
            this.playSound(ModSounds.ENTITY_FAIRY_AMBIENT.get(), 0.4f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.3f);
        }
    }
}
