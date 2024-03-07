package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpike;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EarthSpikeSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntitySpike spike = new EntitySpike(level, entity, 30, 5, EntitySpike.SpikeType.EARTH);
        spike.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.2f));
        if (entity instanceof Player player) {
            HitResult result = RayTraceUtils.entityRayTrace(player, 12, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, true, false, e -> e instanceof LivingEntity);
            if (result instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof LivingEntity target) {
                spike.setEntityTarget(target);
            }
        }
        level.addFreshEntity(spike);
        playSound(entity, ModSounds.SPELL_GENERIC_ROCKS.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
