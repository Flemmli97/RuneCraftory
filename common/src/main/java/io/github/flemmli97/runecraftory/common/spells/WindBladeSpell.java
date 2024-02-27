package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class WindBladeSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityWindBlade wind = new EntityWindBlade(level, entity);
        wind.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        wind.shoot(entity, 0, entity.getYRot(), 0, 0.45f, 0);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            wind.setTarget(mob.getTarget());
        } else if (entity instanceof Player) {
            EntityHitResult res = calculateEntityFromLook(entity, 10);
            if (res != null) {
                wind.setTarget(res.getEntity());
            }
        }
        level.addFreshEntity(wind);
        playSound(entity, ModSounds.SPELL_GENERIC_WIND.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    public static EntityHitResult calculateEntityFromLook(LivingEntity entity, float reach) {
        Vec3 pos = entity.getEyePosition(1);
        Vec3 dir = entity.getViewVector(1);
        Vec3 scaledDir = dir.scale(reach);
        EntityHitResult result = RayTraceUtils.rayTraceEntities(entity.level, entity, pos, pos.add(scaledDir), entity.getBoundingBox().expandTowards(scaledDir).inflate(1), (t) -> EntitySelector.NO_SPECTATORS.test(t) && t.isPickable(), e -> e.getPickRadius() + 1);
        if (result != null) {
            Vec3 loc = result.getLocation();
            double dist = pos.distanceToSqr(loc);
            if (dist <= reach * reach)
                return result;
        }
        return null;
    }
}
