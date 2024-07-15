package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireWallSummoner extends ProjectileSummonHelperEntity {

    public FireWallSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public FireWallSummoner(Level level, LivingEntity caster) {
        super(ModEntities.FIRE_WALL_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 35;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.ticksExisted == 5) {
            Vec3 dir = new Vec3(this.targetX, this.targetY, this.targetZ).subtract(this.position()).normalize().scale(1.8);
            ((ServerLevel) this.level).sendParticles(new ColoredParticleData(ModParticles.STATIC_LIGHT.get(), 255 / 255F, 255 / 255F, 255 / 255F, 1, 1), this.getX(), this.getY(), this.getZ(), 0, dir.x(), dir.y(), dir.z(), 1);
        }
    }

    @Override
    protected void summonProjectiles() {
        LivingEntity owner = this.getOwner();
        if (this.ticksExisted > 10 && this.ticksExisted % 2 == 0) {
            Vec3 dir = new Vec3(this.targetX, this.targetY, this.targetZ).subtract(this.position()).normalize();
            EntityElementalTrail fire = new EntityElementalTrail(this.level, owner, EnumElement.FIRE);
            fire.setPos(this.position().add(dir.scale((this.ticksExisted - 10) / 2f * 1)));
            this.playSound(SoundEvents.BLAZE_SHOOT, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.level.addFreshEntity(fire);

        }
    }
}
