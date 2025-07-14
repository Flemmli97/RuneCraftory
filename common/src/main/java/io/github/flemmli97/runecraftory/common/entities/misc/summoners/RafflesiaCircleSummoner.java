package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.StatusBallEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RafflesiaCircleSummoner extends ProjectileSummonHelperEntity {

    public RafflesiaCircleSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public RafflesiaCircleSummoner(Level level, LivingEntity caster) {
        super(RuneCraftoryEntities.RAFFLESIA_CIRCLE_SUMMONER.get(), level, caster);
        this.damageMultiplier = 0.8f;
        this.maxLivingTicks = 360 / 10;
    }

    @Override
    protected void summonProjectiles() {
        float rot = this.getYRot() + this.tickCount * 10;
        StatusBallEntity ball = new StatusBallEntity(this.level(), this.getOwner());
        ball.setType(StatusBallEntity.Type.RAFFLESIA_ALL);
        ball.setDamageMultiplier(this.damageMultiplier);
        ball.shootFromRotation(this, 0, rot, 0, 0.3f, 0);
        ball.setPos(ball.getX(), this.getY(), ball.getZ());
        this.level().addFreshEntity(ball);
    }
}
