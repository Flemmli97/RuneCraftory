package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.entities.misc.WaterLaserEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class TripleWaterLaserSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = -1; i < 2; i++) {
            WaterLaserEntity laser = new WaterLaserEntity(level, entity, new Vector3f(0, 0, 0.5f));
            laser.setMaxTicks(entity instanceof Player ?
                    Mth.ceil(PlayerModelAnimations.WATER_LASER_THREE.length()) : 15);
            laser.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.2f));
            laser.setYawOffset(i * 130);
            laser.setRotationFromOffset();
            level.addFreshEntity(laser);
        }
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_WATER.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    @Override
    public AttackAction useAction() {
        return RuneCraftoryAttackActions.TRIPLE_WATER_LASER_USE.get();
    }
}
