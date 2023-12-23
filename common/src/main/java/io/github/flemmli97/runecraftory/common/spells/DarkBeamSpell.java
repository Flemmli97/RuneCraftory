package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBeam;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class DarkBeamSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityDarkBeam beam = new EntityDarkBeam(level, entity);
        beam.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
        if (entity instanceof Mob mob && mob.getTarget() != null)
            beam.setRotationTo(mob.getTarget(), 0);
        else {
            beam.setYRot((entity.yHeadRot - 180) % 360.0F);
            beam.setXRot(entity.getXRot() % 360.0F);
        }
        level.addFreshEntity(beam);
        return true;
    }
}
