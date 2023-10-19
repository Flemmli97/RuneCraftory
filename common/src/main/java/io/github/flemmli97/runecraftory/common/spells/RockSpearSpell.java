package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRockSpear;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RockSpearSpell extends Spell {

    public final boolean big;

    public RockSpearSpell(boolean big) {
        this.big = big;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityRockSpear spear = new EntityRockSpear(level, entity, this.big);
        spear.setDamageMultiplier(0.95f + lvl * 0.05f + (this.big ? 0.1f : 0));
        level.addFreshEntity(spear);
        return true;
    }
}
