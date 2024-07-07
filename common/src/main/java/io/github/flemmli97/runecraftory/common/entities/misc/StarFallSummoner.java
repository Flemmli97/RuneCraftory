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
        this.damageMultiplier = 1;
    }

    public void setMaxLivingTicks(int maxLivingTicks) {
        this.maxLivingTicks = maxLivingTicks;
    }

    @Override
    protected void summonProjectiles() {
        if (this.tickCount % 10 == 0) {
            for (int i = 0; i < 12; i++) {
                EntityStarfall proj = new EntityStarfall(this.level, this.getOwner());
                proj.setDamageMultiplier(this.damageMultiplier);
                double randX = this.getX() + this.random.nextDouble() * 24 - 12;
                double randZ = this.getZ() + this.random.nextDouble() * 24 - 12;
                proj.setPos(randX, this.getY() + 11 + this.random.nextDouble() * 2, randZ);
                this.level.addFreshEntity(proj);
            }
            this.playSound(ModSounds.SPELL_STARFALL_AMBIENT.get(), 0.4f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }
}
