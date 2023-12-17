package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityPowerWave;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PowerWaveSpell extends WeaponSpell {

    public PowerWaveSpell() {
        super(ModAttackActions.POWER_WAVE, ModTags.SHORTSWORDS);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean result = super.use(level, entity, stack, rpUseMultiplier, amount, lvl);
        if (result) {
            EntityPowerWave wave = new EntityPowerWave(level, entity);
            wave.setPos(wave.getX(), entity.getY() + entity.getBbHeight() * 0.1, wave.getZ());
            wave.setDamageMultiplier(1.1f + (lvl - 1) * 0.05f);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                wave.shootAtEntity(mob.getTarget(), 0.9f, 0, 0);
            } else {
                Vec3 dir = entity.getEyePosition().add(Vec3.directionFromRotation(entity.getXRot() + 5, entity.getYRot()).scale(10));
                wave.shootAtPosition(dir.x(), dir.y(), dir.z(), 0.9f, 0);
            }
            level.addFreshEntity(wave);
        }
        return result;
    }
}
