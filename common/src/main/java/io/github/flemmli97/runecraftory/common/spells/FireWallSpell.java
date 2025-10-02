package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.FireWallSummoner;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FireWallSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        FireWallSummoner wall = new FireWallSummoner(level, entity);
        wall.setPos(offset(entity));
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null)
            target = entity.position().add(entity.getViewVector(1).scale(10));
        wall.setTarget(target.x, target.y, target.z);
        wall.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        level.addFreshEntity(wall);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    public static Vec3 offset(LivingEntity entity) {
        Vec3 pos = entity.position().add(0, entity.getEyeHeight(), 0);
        Vec3 lookDir = EntityUtils.horizontalLookAngle(entity).scale(entity.getBbWidth() * 0.8);
        return pos.add(lookDir).add(0, -entity.getBbHeight() * 0.2, 0);
    }
}
