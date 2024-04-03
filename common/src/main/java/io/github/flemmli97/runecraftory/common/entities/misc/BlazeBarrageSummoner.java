package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlazeBarrageSummoner extends ProjectileSummonHelperEntity {

    public BlazeBarrageSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BlazeBarrageSummoner(Level level, LivingEntity caster) {
        super(ModEntities.BLAZE_BARRAGE.get(), level, caster);
        this.maxLivingTicks = 15;
    }

    @Override
    protected void summonProjectiles() {
        if (this.ticksExisted % 5 == 0) {
            LivingEntity owner = this.getOwner();
            if (owner == null)
                return;
            Vec3 look;
            if (owner instanceof Mob mob && mob.getTarget() != null)
                look = new Vec3(mob.getTarget().getX() - owner.getX(), mob.getTarget().getY() - owner.getY(), mob.getTarget().getZ() - owner.getZ()).normalize();
            else
                look = owner.getLookAngle();
            float inaccuracy = 5;
            Vec3 vec3 = look.normalize().add(this.random.nextGaussian() * 0.0075 * inaccuracy,
                    this.random.nextGaussian() * 0.0075 * inaccuracy, this.random.nextGaussian() * 0.0075 * inaccuracy).scale(2);
            SmallFireball fireball = new SmallFireball(this.level, owner, vec3.x, vec3.y, vec3.z);
            fireball.setPos(fireball.getX(), owner.getY(0.5) + 0.5, fireball.getZ());
            this.playSound(SoundEvents.BLAZE_SHOOT, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.level.addFreshEntity(fireball);
        }
    }
}
