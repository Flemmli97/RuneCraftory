package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SteelHeartSpell extends WeaponSpell {

    public SteelHeartSpell() {
        super(RuneCraftoryAttackActions.STEEL_HEART, RunecraftoryTags.Items.LONGSWORDS);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean result = super.use(level, entity, stack, rpUseMultiplier, amount, lvl);
        if (result) {
            entity.addEffect(new MobEffectInstance(RuneCraftoryEffects.STEEL_HEART.asHolder(), 400 + 30 * lvl, 0));
            playSound(entity, RuneCraftorySounds.SPELL_GENERIC_BUFF.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        }
        return result;
    }
}