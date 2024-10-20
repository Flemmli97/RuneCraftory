package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAppleProjectile;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AppleShield extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int apples = 12;
        for (int i = 0; i < apples; i++) {
            EntityAppleProjectile apple = new EntityAppleProjectile(level, entity);
            apple.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
            apple.setAngleOffset(360 / apples * i);
            apple.setCircling(true, 80 + i * 3);
            level.addFreshEntity(apple);
        }
        return true;
    }
}
