package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class WindBladeBarrageSummoner extends ProjectileSummonHelperEntity {

    public WindBladeBarrageSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public WindBladeBarrageSummoner(Level level, LivingEntity caster) {
        super(ModEntities.WIND_BLADE_BARRAGE_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 15;
    }

    @Override
    protected void summonProjectiles() {
        int amount = this.random.nextInt(3) + 2;
        for (int i = 0; i < amount; i++) {
            EntityWindBlade wind = new EntityWindBlade(this.level, this.getOwner());
            wind.setPos(this.getX() + (2.0 * this.random.nextDouble() - 1.0) * 1.2, this.getY(), this.getZ() + (2.0 * this.random.nextDouble() - 1.0) * 1.2);
            wind.setDamageMultiplier(this.damageMultiplier);
            wind.setType(EntityWindBlade.Type.PLAIN);
            wind.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.7f, 12);
            this.level.addFreshEntity(wind);
        }
    }
}
