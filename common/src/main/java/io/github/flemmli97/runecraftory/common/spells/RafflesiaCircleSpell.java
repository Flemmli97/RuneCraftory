package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.RafflesiaCircleSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RafflesiaCircleSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        RafflesiaCircleSummoner summoner = new RafflesiaCircleSummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        Vec3 position = entity.position().add(0, Math.min(0.4, entity.getBbHeight() * 0.4), 0);
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null) {
            target = position.add(entity.getLookAngle().scale(5));
        }
        summoner.setPos(position.x, position.y, position.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}