package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.StarFallSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StarFallSpell extends Spell {

    private final boolean longVersion;

    public StarFallSpell(boolean longVersion) {
        this.longVersion = longVersion;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        StarFallSummoner summoner = new StarFallSummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
        summoner.setMaxLivingTicks(this.longVersion ? 140 : 60);
        level.addFreshEntity(summoner);
        return true;
    }
}