package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.entities.misc.WaterLaserEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WaterLaserSpell extends Spell {

    private final float range;

    public WaterLaserSpell(float range) {
        this.range = range;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        WaterLaserEntity laser = new WaterLaserEntity(level, entity);
        laser.setMaxTicks(entity instanceof Player ? Mth.ceil(PlayerModelAnimations.ANIMS.get(PlayerModelAnimations.WATER_LASER_ONE).length()) : 15);
        laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        laser.setRange(this.range);
        ProjectileUtils.shoot(entity, laser, 0);
        level.addFreshEntity(laser);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_WATER.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    @Override
    public AttackAction useAction() {
        return RuneCraftoryAttackActions.WATER_LASER_USE.get();
    }
}
