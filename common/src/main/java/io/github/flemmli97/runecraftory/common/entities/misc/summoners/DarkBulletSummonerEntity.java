package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.DarkBulletEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DarkBulletSummonerEntity extends ProjectileSummonHelperEntity {

    public DarkBulletSummonerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public DarkBulletSummonerEntity(Level level, LivingEntity caster) {
        super(RuneCraftoryEntities.DARK_BULLET_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 18;
    }

    @Override
    protected void summonProjectiles() {
        if (this.random.nextInt(3) == 0)
            return;
        DarkBulletEntity bullet = new DarkBulletEntity(this.level(), this.getOwner());
        bullet.setPos(this.getX(), this.getY(), this.getZ());
        bullet.shootAtPosition(this.targetX, this.targetY, this.targetZ, 1.2f, 0);
        bullet.setDamageMultiplier(this.damageMultiplier);
        bullet.setPos(bullet.getX() + this.random.nextFloat() * 1.3 - 0.65, bullet.getY() + this.random.nextFloat() * 0.05 - 0.1, bullet.getZ() + this.random.nextFloat() * 1.3 - 0.65);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARROW_SHOOT, this.getSoundSource(), 1.0f, 1.5f + this.level().getRandom().nextFloat() * 0.1f);
        this.level().addFreshEntity(bullet);
    }
}
