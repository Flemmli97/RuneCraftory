package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ExpandingLight extends Spell {

    private final int amount;

    public ExpandingLight(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityLightBall.createLights(level, entity, EntityLightBall.Type.EXPAND, CombatUtils.getAbilityDamageBonus(lvl, 1.1f), this.amount);
        playSound(entity, ModSounds.SPELL_GENERIC_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
