package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ButterflyEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ButterflySummonerEntity extends ProjectileSummonHelperEntity {

    public ButterflySummonerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ButterflySummonerEntity(Level level, LivingEntity caster) {
        super(RuneCraftoryEntities.BUTTERFLY_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 26;
    }

    @Override
    protected void summonProjectiles() {
        ButterflyEntity fly = new ButterflyEntity(this.level(), this.getOwner());
        fly.setDamageMultiplier(this.damageMultiplier);
        fly.setPos(fly.getX() + this.random.nextFloat() * 2 - 1, fly.getY() + this.random.nextFloat() * 0.05 - 0.1, fly.getZ() + this.random.nextFloat() * 2 - 1);
        fly.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.3f, 12);
        this.playSound(RuneCraftorySounds.SPELL_GENERIC_POP.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        this.level().addFreshEntity(fly);
    }
}
