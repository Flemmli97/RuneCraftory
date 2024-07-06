package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityGustRocks;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GustRockSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityGustRocks gust = new EntityGustRocks(level, entity);
        gust.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.15f));
        gust.setPos(entity.getX(), entity.getY(), entity.getZ());
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 targetPos = EntityUtil.getStraightProjectileTarget(gust.position(), mob.getTarget());
            Vec3 dir = (new Vec3(targetPos.x() - gust.getX(), targetPos.y() - gust.getY(), targetPos.z() - gust.getZ()));
            gust.setDirection(dir.x, dir.y, dir.z);
        } else {
            gust.setDirection(entity.getXRot(), entity.getYRot());
        }
        level.addFreshEntity(gust);
        playSound(entity, ModSounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
