package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.ThiccLightningBoltEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BigLightningBoltSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ThiccLightningBoltEntity bolt = new ThiccLightningBoltEntity(level, entity);
        bolt.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
        ProjectileUtils.shoot(entity, bolt, 0.2f, 0);
        level.addFreshEntity(bolt);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_ELECTRIC_ZAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.7f);
        return true;
    }
}
