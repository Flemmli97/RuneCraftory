package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.SwipingWaterLaserEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WaterLaserSwipe extends Spell {

    private final int duration;
    private final float angle;

    public WaterLaserSwipe(int duration, float angle) {
        this.duration = duration;
        this.angle = angle;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        float angle = this.angle * 2;
        angle = entity instanceof MobAttackExt ext && ext.reversed() ? angle : -angle;
        SwipingWaterLaserEntity laser = new SwipingWaterLaserEntity(level, entity, angle).setMaxTicks(this.duration);
        laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(laser.position());
        } else {
            dir = entity.getLookAngle();
        }
        float offset = entity instanceof MobAttackExt ext && ext.reversed() ? -this.angle : this.angle;
        laser.setRotationToDirWithOffset(dir.x(), dir.y(), dir.z(), 0, offset);
        level.addFreshEntity(laser);
        return true;
    }
}