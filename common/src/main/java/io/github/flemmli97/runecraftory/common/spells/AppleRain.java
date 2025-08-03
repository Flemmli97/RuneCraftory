package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.AppleRainSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AppleRain extends Spell {

    private final Type type;

    public AppleRain(Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        AppleRainSummoner summoner = new AppleRainSummoner(level, entity, this.type);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        level.addFreshEntity(summoner);
        return true;
    }

    public enum Type {
        NORMAL,
        BIG,
        LOTS
    }
}
