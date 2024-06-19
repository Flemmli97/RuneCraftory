package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class BigLightningBoltSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityThiccLightningBolt bolt = new EntityThiccLightningBolt(level, entity);
        bolt.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            bolt.shootAtEntity(mob.getTarget(), 0.2f, 0, 0);
        } else
            bolt.shoot(entity, 17, entity.getYRot(), 0, 0.2f, 0);
        level.addFreshEntity(bolt);
        playSound(entity, ModSounds.SPELL_GENERIC_ELECTRIC_ZAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.7f);
        return true;
    }
}
