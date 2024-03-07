package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpike;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RootSpike extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntitySpike spike = new EntitySpike(level, entity, 0, 10, EntitySpike.SpikeType.ROOT);
        spike.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        Vec3 targetPos = null;
        if (entity instanceof Mob mob) {
            Entity target = EntityUtils.ownedProjectileTarget(mob, 14);
            if (target != null)
                targetPos = target.position();
        } else if (entity instanceof Player player) {
            HitResult result = RayTraceUtils.entityRayTrace(player, 12, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, true, false, e -> e instanceof LivingEntity);
            if (result != null) {
                targetPos = result.getLocation();
            }
        }
        if (targetPos != null) {
            spike.setPos(targetPos);
        }
        level.addFreshEntity(spike);
        return true;
    }
}
