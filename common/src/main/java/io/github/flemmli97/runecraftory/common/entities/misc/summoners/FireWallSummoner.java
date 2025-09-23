package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ElementalTrailEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
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
        super(RuneCraftoryEntities.FIRE_WALL_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 35;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.ticksExisted == 5) {
            Vec3 dir = new Vec3(this.targetX, this.targetY, this.targetZ).subtract(this.position()).normalize().scale(1.8);
            AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                    .addData(new ColorData(255 / 255F, 255 / 255F, 255 / 255F, 1))
                    .addData(new ScaleData(0.5f))
                    .addData(new MotionData(dir.x(), dir.y(), dir.z()))
                    .addData(new ParticleMetaData(20, false, 0))
                    .add(this.level(), this.getX(), this.getY(), this.getZ());
        }
    }

    @Override
    protected void summonProjectiles() {
        LivingEntity owner = this.getOwner();
        if (this.ticksExisted > 10 && this.ticksExisted % 2 == 0) {
            Vec3 dir = new Vec3(this.targetX, this.targetY, this.targetZ).subtract(this.position()).normalize();
            ElementalTrailEntity fire = new ElementalTrailEntity(this.level(), owner, ItemElement.FIRE);
            fire.setPos(this.position().add(dir.scale((this.ticksExisted - 10) / 2f * 1)));
            this.playSound(SoundEvents.BLAZE_SHOOT, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.level().addFreshEntity(fire);
        }
    }
}
