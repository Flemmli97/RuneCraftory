package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySleepAura;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SleepAuraSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost(), EnumSkills.EARTH))
            return false;
        EntitySleepAura aura = new EntitySleepAura(level, entity);
        aura.setDamageMultiplier(0.7f + lvl * 0.075f);
        level.addFreshEntity(aura);
        return true;
    }
}
