package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityIceTrail;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class IceTrailSpell extends Spell {

    private final boolean homing;

    public IceTrailSpell(boolean homing) {
        this.homing = homing;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityIceTrail ice = new EntityIceTrail(level, entity);
        ice.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1f));
        ice.setHoming(this.homing);
        level.addFreshEntity(ice);
        return true;
    }
}
