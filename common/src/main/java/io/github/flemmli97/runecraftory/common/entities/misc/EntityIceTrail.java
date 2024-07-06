package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityIceTrail extends BaseDamageCloud {

    private boolean homing = true;
    private LivingEntity targetMob;

    public EntityIceTrail(EntityType<? extends BaseDamageCloud> type, Level world) {
        super(type, world);
        this.setRadius(0.5f);
    }

    public EntityIceTrail(Level world, LivingEntity shooter) {
        super(ModEntities.ICE_TRAIL.get(), world, shooter);
        this.setPos(this.getX(), this.getY() - 0.5, this.getZ());
        this.setRadius(0.5f);
    }

    public void setHoming(boolean homing) {
        this.homing = homing;
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        return CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).magic().element(EnumElement.WATER).noKnockback().hurtResistant(10), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        this.setPos(newX, newY, newZ);
        if (!this.level.isClientSide) {
            if (this.homing) {
                if (this.targetMob == null || this.targetMob.isDeadOrDying()) {
                    this.targetMob = EntityUtils.ownedProjectileTarget(this.getOwner(), 10);
                } else {
                    Vec3 dir = this.targetMob.position().add(0, -0.6, 0).subtract(this.position());
                    if (dir.lengthSqr() > 0.22 * 0.22)
                        dir = dir.normalize().scale(0.22);
                    this.setDeltaMovement(dir);
                    this.hasImpulse = true;
                }
            }
        } else {
            if (this.livingTicks % 2 == 0)
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, this.getSoundSource(), 2, 0.8f, false);
        }
    }
}
