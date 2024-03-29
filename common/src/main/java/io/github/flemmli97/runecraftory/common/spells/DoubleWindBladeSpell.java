package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class DoubleWindBladeSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = 0; i < 2; i++) {
            EntityWindBlade wind = new EntityWindBlade(level, entity);
            wind.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
            wind.shoot(entity, 0, entity.getYRot() - (i == 0 ? 1 : -1) * 40, 0, 0.45f, 0);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                wind.setTarget(mob.getTarget());
            } else if (entity instanceof Player) {
                EntityHitResult res = WindBladeSpell.calculateEntityFromLook(entity, 10);
                if (res != null) {
                    wind.setTarget(res.getEntity());
                }
            }
            level.addFreshEntity(wind);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_WIND.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
