package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WaterLaserSpell extends Spell {

    private final float range;

    public WaterLaserSpell(float range) {
        this.range = range;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityWaterLaser laser = new EntityWaterLaser(level, entity);
        laser.setMaxTicks(entity instanceof Player ? PlayerModelAnimations.WATER_LASER_ONE.getLength() : 15);
        laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
        laser.setRange(this.range);
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (entity instanceof MobAttackExt attacker && (delayedPos = attacker.targetPosition(laser.position())) != null) {
                laser.setRotationTo(delayedPos.x(), delayedPos.y(), delayedPos.z(), 0);
            } else if (mob.getTarget() != null) {
                LivingEntity target = mob.getTarget();
                laser.setRotationTo(target, 0);
            }
        }
        level.addFreshEntity(laser);
        playSound(entity, ModSounds.SPELL_GENERIC_WATER.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    @Override
    public AttackAction useAction() {
        return ModAttackActions.WATER_LASER_USE.get();
    }
}
