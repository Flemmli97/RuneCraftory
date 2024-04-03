package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.BlazeBarrageSummoner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BlazeFireballsSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        level.addFreshEntity(new BlazeBarrageSummoner(level, entity));
        return true;
    }
}