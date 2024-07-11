package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBeam;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class LightBeamSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityLightBeam beam = new EntityLightBeam(level, entity);
        beam.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        if (entity instanceof Mob mob && mob.getTarget() != null)
            beam.setRotationTo(mob.getTarget(), 1);
        else {
            float yRot = (float) ((entity.yHeadRot - 180 + entity.getRandom().nextGaussian() * 5) % 360.0F);
            float xRot = (float) ((entity.getXRot() + entity.getRandom().nextGaussian() * 3) % 360.0F);
            beam.setYRot(yRot);
            beam.setXRot(xRot);
        }
        level.addFreshEntity(beam);
        playSound(entity, ModSounds.SPELL_GENERIC_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
