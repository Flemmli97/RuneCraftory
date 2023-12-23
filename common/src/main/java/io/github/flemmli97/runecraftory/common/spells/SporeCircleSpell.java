package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.SporeCircleSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SporeCircleSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        SporeCircleSummoner summoner = new SporeCircleSummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1f));
        summoner.setPos(entity.position().x, entity.position().y, entity.position().z);
        level.addFreshEntity(summoner);
        return true;
    }
}