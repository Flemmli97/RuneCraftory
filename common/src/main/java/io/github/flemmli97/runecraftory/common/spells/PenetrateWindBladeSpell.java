package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PenetrateWindBladeSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = 0; i < 3; i++) {
            EntityWindBlade wind = new EntityWindBlade(level, entity);
            wind.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
            wind.setPiercing();
            wind.shoot(entity, entity.getXRot(), entity.getYRot() - (i - 1) * 50, 0, 0.35f, 0);
            level.addFreshEntity(wind);
        }
        return true;
    }
}
