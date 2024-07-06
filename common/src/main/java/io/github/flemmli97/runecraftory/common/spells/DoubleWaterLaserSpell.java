package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class DoubleWaterLaserSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 dir = entity.getLookAngle();
        Vec3 pos = entity.position().add(0, entity.getEyeHeight() - 0.1, 0);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            LivingEntity target = mob.getTarget();
            Vec3 targetPos = EntityUtil.getStraightProjectileTarget(pos, target);
            dir = targetPos.subtract(pos);
        }
        UUID other = null;
        for (int i = 0; i < 2; i++) {
            float posYawOff = (i == 0 ? -1 : 1) * 30;
            Vector3f vec = RayTraceUtils.rotatedAround(dir.normalize(), Vector3f.YP, posYawOff);
            EntityWaterLaser laser = new EntityWaterLaser(level, entity);
            if (i == 0)
                other = laser.getUUID();
            else laser.setTwinId(other);
            laser.setPos(laser.getX() + vec.x(), laser.getY() + vec.y(), laser.getZ() + vec.z());
            laser.setMaxTicks(entity instanceof Player ? PlayerModelAnimations.WATER_LASER_TWO.getLength() : 15);
            laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
            Vec3 target = laser.position().add(dir);
            laser.setRotationTo(target.x(), target.y(), target.z(), 0);
            laser.setPositionYawOffset(posYawOff);
            level.addFreshEntity(laser);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_WATER.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    @Override
    public AttackAction useAction() {
        return ModAttackActions.DOUBLE_WATER_LASER_USE.get();
    }
}
