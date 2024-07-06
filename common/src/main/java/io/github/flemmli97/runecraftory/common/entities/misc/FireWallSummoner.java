package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
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
        super(ModEntities.BLAZE_BARRAGE.get(), level, caster);
        this.maxLivingTicks = 30;
    }

    @Override
    protected void summonProjectiles() {
        LivingEntity owner = this.getOwner();
        if (owner == null)
            return;
        if (this.ticksExisted == 5) {

        } else if (this.ticksExisted > 5 && this.ticksExisted % 2 == 0) {
            Vec3 dir = new Vec3(this.targetX, this.targetY, this.targetZ).subtract(this.position()).normalize();
            EntityWispFlame fire = new EntityWispFlame(this.level, owner, EnumElement.FIRE);
            fire.setPos(this.position().add(dir.scale((this.ticksExisted - 2) / 2f * 1)));
            this.playSound(SoundEvents.BLAZE_SHOOT, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.level.addFreshEntity(fire);

        }
    }
}
