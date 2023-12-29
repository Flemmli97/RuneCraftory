package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SteelHeartSpell extends WeaponSpell {

    public SteelHeartSpell() {
        super(ModAttackActions.STEEL_HEART, ModTags.LONGSWORDS);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean result = super.use(level, entity, stack, rpUseMultiplier, amount, lvl);
        if (result) {
            entity.addEffect(new MobEffectInstance(ModEffects.STEEL_HEART.get(), 400 + 30 * lvl, 0));
        }
        return result;
    }
}