package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.StarfallEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class StarFallSummoner extends ProjectileSummonHelperEntity {

    public StarFallSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public StarFallSummoner(Level level, LivingEntity caster) {
        super(RuneCraftoryEntities.STARFALL_SUMMONER.get(), level, caster);
    }

    public void setMaxLivingTicks(int maxLivingTicks) {
        this.maxLivingTicks = maxLivingTicks;
    }

    @Override
    protected void summonProjectiles() {
        if (this.tickCount % 4 == 0) {
            for (int i = 0; i < 24; i++) {
                double randX = this.random.nextDouble() * 48 - 24;
                double randZ = this.random.nextDouble() * 48 - 24;
                if (randX * randX + randZ * randZ > 16 * 16)
                    continue;
                StarfallEntity proj = new StarfallEntity(this.level(), this.getOwner());
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.setPos(this.getX() + randX, this.getY() + 11 + this.random.nextDouble() * 2, this.getZ() + randZ);
                this.level().addFreshEntity(proj);
            }
            this.playSound(RuneCraftorySounds.ENTITY_FAIRY_AMBIENT.get(), 0.4f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.3f);
        }
    }
}
