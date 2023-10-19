package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TripleWaterLaserSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = -1; i < 2; i++) {
            float posYawOff = i * 130;
            Vector3f vec = RayTraceUtils.rotatedAround(entity.getLookAngle(), Vector3f.YP, posYawOff);
            EntityWaterLaser laser = new EntityWaterLaser(level, entity);
            laser.setPos(laser.getX() + vec.x(), laser.getY() + vec.y(), laser.getZ() + vec.z());
            laser.setMaxTicks(entity instanceof Player ? 44 : 15);
            laser.setDamageMultiplier(0.95f + lvl * 0.05f);
            laser.setYawOffset(-i * 130);
            laser.setPositionYawOffset(posYawOff);
            level.addFreshEntity(laser);
        }
        return true;
    }
}
