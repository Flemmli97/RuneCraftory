package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.RootSpikeSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RootSpikeTriple extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        RootSpikeSummoner summoner = new RootSpikeSummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.85f));
        level.addFreshEntity(summoner);
        return true;
    }
}
